package org.nishen.resourcepartners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nishen.resourcepartners.dao.DatastoreDAO;
import org.nishen.resourcepartners.entity.ResourcePartner;
import org.nishen.resourcepartners.entity.ResourcePartnerChangeRecord;
import org.nishen.resourcepartners.harvesters.Harvester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

/**
 * @author nishen.naidoo
 *
 */

public class ResourcePartnerHarvesterImpl implements ResourcePartnerHarvester
{
	private static final Logger log = LoggerFactory.getLogger(ResourcePartnerHarvesterImpl.class);

	private DatastoreDAO datastore;

	private Set<Harvester> harvesters;

	@Inject
	public ResourcePartnerHarvesterImpl(DatastoreDAO datastore, Set<Harvester> harvesters)
	{
		this.datastore = datastore;
		this.harvesters = harvesters;

		log.debug("instantiated ResourcePartnerProcessor");
	}

	@Override
	public void process(Map<String, String> options) throws Exception
	{
		Map<String, ResourcePartner> partners = datastore.getPartners();
		log.info("loaded partners from datastore: {}", partners.size());

		Set<String> harvestersToProcess = new HashSet<String>();

		Set<String> harvestersPresent = new HashSet<String>();
		for (Harvester harvester : harvesters)
			harvestersPresent.add(harvester.getSource());

		Set<String> harvesterSet = null;
		String harvesterOption = options.get("harvesters");
		if (harvesterOption != null)
			harvesterSet = new HashSet<String>(Arrays.asList(harvesterOption.split(",")));

		if (harvesterSet != null && harvesterSet.size() > 0)
		{
			for (String h : harvesterSet)
			{
				if (harvestersPresent.contains(h))
					harvestersToProcess.add(h);
				else
					throw new Exception("invalid harvester specified: " + h);
			}
		}
		else
		{
			harvestersToProcess = new HashSet<String>(harvestersPresent);
		}

		for (Harvester harvester : harvesters)
		{
			if (!harvestersToProcess.contains(harvester.getSource()))
			{
				log.debug("skipping harvester [{}]: harvester filtered out on command line", harvester.getSource());
				continue;
			}

			try
			{
				log.info("harvesting from: {}", harvester.getSource());
				Map<String, ResourcePartner> harvestedPartners = harvester.harvest();

				List<ResourcePartnerChangeRecord> changes = new ArrayList<ResourcePartnerChangeRecord>();

				log.info("partners found:     {}", harvestedPartners.size());
				Map<String, ResourcePartner> changed = harvester.update(partners, harvestedPartners, changes);
				partners.putAll(changed);
				log.info("partners changed:   {}", changed.size());

				if (changed.size() > 0)
				{
					datastore.addEntities(changed.values());
					log.info("partners saved: {}", changed.size());
				}
			}
			catch (ResourcePartnerHarvesterSkipException sre)
			{
				log.info("skipping harvesting: {}", harvester.getSource());
			}
		}
	}
}
