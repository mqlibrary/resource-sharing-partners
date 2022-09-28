package org.nishen.resourcepartners.harvesters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.nishen.resourcepartners.dao.LaddDAO;
import org.nishen.resourcepartners.entity.ResourcePartner;
import org.nishen.resourcepartners.entity.ResourcePartnerChangeRecord;
import org.nishen.resourcepartners.entity.ResourcePartnerSuspension;
import org.nishen.resourcepartners.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

/**
 * @author nishen
 *
 */
public class HarvesterLadd implements Harvester
{
	private static final Logger log = LoggerFactory.getLogger(HarvesterLadd.class);

	private static final String SOURCE_SYSTEM = "LADD";

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);

	private ObjectMapper om = new ObjectMapper();

	private LaddDAO ladd;

	@Inject
	public HarvesterLadd(LaddDAO ladd)
	{
		this.ladd = ladd;

		log.debug("instantiated class: {}", this.getClass().getName());
	}

	@Override
	public String getSource()
	{
		return SOURCE_SYSTEM;
	}

	@Override
	public Map<String, ResourcePartner> harvest()
	{
		return ladd.getData();
	}

	@Override
	public Map<String, ResourcePartner> update(Map<String, ResourcePartner> partners,
	                                           Map<String, ResourcePartner> latest,
	                                           List<ResourcePartnerChangeRecord> changes)
	{
		Map<String, ResourcePartner> updated = new HashMap<String, ResourcePartner>();

		List<String> removeList = new ArrayList<String>();
		for (String s : partners.keySet())
			if (!s.startsWith(NZ_NUC_PREFIX))
				removeList.add(s);

		for (String nuc : latest.keySet())
		{
			removeList.remove(nuc);

			ResourcePartner l = latest.get(nuc);
			ResourcePartner p = partners.get(nuc);

			boolean requiresUpdate = false;

			try
			{
				if (p == null)
				{
					l.setUpdated(sdf.format(new Date()));
					updated.put(nuc, l);
					changes.add(new ResourcePartnerChangeRecord(SOURCE_SYSTEM, nuc, "partner", null,
					                                            om.writeValueAsString(l)));

					continue;
				}

				if (p.isEnabled() != l.isEnabled())
				{
					changes.add(new ResourcePartnerChangeRecord(SOURCE_SYSTEM, nuc, "enabled",
					                                            Boolean.toString(p.isEnabled()),
					                                            Boolean.toString(l.isEnabled())));
					p.setEnabled(l.isEnabled());
					requiresUpdate = true;
				}

				if (!ObjectUtil.compareStrings(p.getName(), l.getName()))
				{
					changes.add(new ResourcePartnerChangeRecord(SOURCE_SYSTEM, nuc, "name", p.getName(), l.getName()));
					p.setName(l.getName());
					requiresUpdate = true;
				}

				if (!ObjectUtil.compareStrings(p.getStatus(), l.getStatus()))
				{
					changes.add(new ResourcePartnerChangeRecord(SOURCE_SYSTEM, nuc, "status", p.getStatus(),
					                                            l.getStatus()));
					p.setStatus(l.getStatus());
					requiresUpdate = true;
				}

				Set<ResourcePartnerSuspension> lSuspensions = new TreeSet<>(l.getSuspensions());
				for (ResourcePartnerSuspension s : lSuspensions)
					if (!p.getSuspensions().contains(s))
					{
						changes.add(new ResourcePartnerChangeRecord(SOURCE_SYSTEM, nuc, "suspension", null,
						                                            om.writeValueAsString(s)));
						p.getSuspensions().add(s);
						requiresUpdate = true;
					}

				if (requiresUpdate)
				{
					if (log.isDebugEnabled())
					{
						log.debug("latest:\n{}\npartner:\n{}\n", om.writeValueAsString(l), om.writeValueAsString(p));
					}

					p.setUpdated(sdf.format(new Date()));
					updated.put(nuc, p);
				}
			}
			catch (JsonProcessingException jpe)
			{
				log.error("{}", jpe.getMessage(), jpe);
			}
		}

		for (String nuc : removeList)
		{
			ResourcePartner p = partners.get(nuc);
			if (p != null && p.isEnabled())
			{
				p.setUpdated(sdf.format(new Date()));
				p.setEnabled(false);
				updated.put(nuc, p);
			}
		}

		return updated;
	}
}
