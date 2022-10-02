package org.nishen.resourcepartners;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.nishen.resourcepartners.entity.ResourcePartnerChangeRecord;
import org.nishen.resourcepartners.entity.SyncPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import static java.util.stream.Collectors.counting;

public class ResourcePartnerApp
{
	private static final Logger log = LoggerFactory.getLogger(ResourcePartnerApp.class);

	private ResourcePartnerHarvester harvester;

	private ResourcePartnerSynchroniser synchroniser;

	private OutputGenerator output;

	@Inject
	private ResourcePartnerApp(ResourcePartnerHarvester harvester, ResourcePartnerSynchroniser synchroniser,
	                           OutputGenerator output)
	{
		this.harvester = harvester;
		this.synchroniser = synchroniser;
		this.output = output;

		log.debug("instantiated class: {}", this.getClass().getName());
	}

	public void run(Map<String, String> options)
	{
		log.debug("application execution starting");

		String action = options.get("action");
		if (action == null || !ResourcePartnerLauncher.ACTIONS.contains(action))
		{
			log.error("invalid action: {}", action);
			return;
		}

		log.info("executing action: {}", action);
		try
		{
			switch (action)
			{
				case "harvest":
					harvester.process(options).ifPresent(this::generateOutput);
					break;

				case "preview":
					synchroniser.sync(true).ifPresent(this::generateOutput);
					break;

				case "sync":
					synchroniser.sync(false).ifPresent(this::generateOutput);
					break;
			}
		}
		catch (Exception e)
		{
			log.error("{}", e.getMessage(), e);
		}

		log.debug("application execution complete");
	}

	public void generateOutput(List<ResourcePartnerChangeRecord> changes)
	{
		try
		{
			long count = changes.stream().collect(counting());
			output.saveHarvestChanges(changes);
			log.info("harvest field changes:    {}", count);
		}
		catch (FileNotFoundException fnfe)
		{
			log.error("unable to save file");
		}
	}

	public void generateOutput(SyncPayload payload)
	{
		try
		{
			output.savePartners(payload.getChanged());
			log.info("partners changed: {}", payload.getChanged().size());

			output.saveDeleted(payload.getDeleted());
			log.info("partners deleted: {}", payload.getDeleted().size());

			int count = payload.getChanges().values().stream().map(l -> l.size()).reduce(0, Integer::sum);
			output.saveChanges(payload.getChanges());
			log.info("field changes:    {}", count);
		}
		catch (FileNotFoundException fnfe)
		{
			log.error("unable to save file");
		}
	}
}
