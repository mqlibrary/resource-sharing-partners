package org.nishen.resourcepartners.harvesters;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.NotFoundException;

import org.nishen.resourcepartners.ResourcePartnerHarvesterSkipException;
import org.nishen.resourcepartners.dao.Config;
import org.nishen.resourcepartners.dao.ConfigFactory;
import org.nishen.resourcepartners.dao.DatastoreDAO;
import org.nishen.resourcepartners.dao.IlrsDAO;
import org.nishen.resourcepartners.entity.ResourcePartner;
import org.nishen.resourcepartners.entity.ResourcePartnerAddress;
import org.nishen.resourcepartners.entity.ResourcePartnerChangeRecord;
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
public class HarvesterIlrs implements Harvester
{
	private static final Logger log = LoggerFactory.getLogger(HarvesterIlrs.class);

	private static final String SOURCE_SYSTEM = "ILRS";

	private static final String NZ_NUC_PREFIX = "NLNZ";

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);

	private static final String DEFAULT_DAYS_BETWEEN = "7";

	private static final int THREADS = 3;

	private static final int BLOCKING_QUEUE_SIZE = 2;

	private ObjectMapper om;

	private Config config;

	private IlrsDAO ilrs;

	private DatastoreDAO datastoreDAO;

	private AtomicInteger harvestCount = new AtomicInteger(0);

	@Inject
	public HarvesterIlrs(ConfigFactory configFactory, IlrsDAO ilrs, DatastoreDAO datastoreDAO)
	{
		this.config = configFactory.fetch(SOURCE_SYSTEM);
		log.debug("config[{}]: {}", SOURCE_SYSTEM, config.getAll());
		this.ilrs = ilrs;
		this.datastoreDAO = datastoreDAO;
		this.om = new ObjectMapper();

		log.debug("instantiated class: {}", this.getClass().getName());
	}

	@Override
	public String getSource()
	{
		return SOURCE_SYSTEM;
	}

	@Override
	public Map<String, ResourcePartner> harvest() throws IOException, ResourcePartnerHarvesterSkipException
	{
		config.set("last_run_attempt", sdf.format(new Date()));
		if (!shouldHarvestRun())
			throw new ResourcePartnerHarvesterSkipException();

		Map<String, ResourcePartner> esPartners = datastoreDAO.getPartners();

		BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_SIZE);
		ExecutorService executor = new ThreadPoolExecutor(THREADS, THREADS, 0L, TimeUnit.MILLISECONDS, queue,
		                                                  new ThreadPoolExecutor.CallerRunsPolicy());

		Map<String, Future<String>> results = new HashMap<String, Future<String>>();
		for (String nuc : esPartners.keySet())
			if (!nuc.startsWith(NZ_NUC_PREFIX))
				results.put(nuc, executor.submit(new Harvester(nuc, harvestCount)));

		executor.shutdown();

		Map<String, ResourcePartner> ilrsPartners = new TreeMap<String, ResourcePartner>();

		for (String nuc : results.keySet())
		{
			try
			{
				String page = results.get(nuc).get();

				if (page == null)
				{
					log.warn("could not obtain ilrs page for: {}", nuc);
					continue;
				}

				Map<String, ResourcePartnerAddress> addresses = ilrs.getAddressesFromPage(page);
				if (log.isDebugEnabled())
					for (String k : addresses.keySet())
						log.debug("result:\n{}", om.writeValueAsString(addresses.get(k)));

				String emailIll = ilrs.getEmailFromPage(page).orElse(null);
				String phoneIll = ilrs.getPhoneIllFromPage(page).orElse(null);
				String phoneFax = ilrs.getPhoneFaxFromPage(page).orElse(null);

				ResourcePartner ep = esPartners.get(nuc);
				ResourcePartner ilrsPartner = new ResourcePartner();
				ilrsPartner.setNuc(ep.getNuc());
				ilrsPartner.setEnabled(ep.isEnabled());
				ilrsPartner.setName(ep.getName());
				ilrsPartner.setStatus(ep.getStatus());
				ilrsPartner.setEmailMain(ep.getEmailMain());
				ilrsPartner.setEmailIll(emailIll);
				ilrsPartner.setPhoneMain(null);
				ilrsPartner.setPhoneIll(phoneIll);
				ilrsPartner.setPhoneFax(phoneFax);
				ilrsPartner.getSuspensions().addAll(ep.getSuspensions());
				for (Map.Entry<String, ResourcePartnerAddress> entry : addresses.entrySet())
				{
					ResourcePartnerAddress address = entry.getValue();
					address.setAddressStatus("active");
					address.setAddressType(entry.getKey());
					ilrsPartner.getAddresses().add(address);
				}
				ilrsPartner.setUpdated(ep.getUpdated());

				ilrsPartners.put(ilrsPartner.getNuc(), ilrsPartner);
			}
			catch (Exception e)
			{
				log.error("error: {}", e.getMessage(), e);
			}
		}

		config.set("last_run", sdf.format(new Date()));

		return ilrsPartners;
	}

	@Override
	public Map<String, ResourcePartner> update(Map<String, ResourcePartner> partners,
	                                           Map<String, ResourcePartner> latest,
	                                           List<ResourcePartnerChangeRecord> changes)
	{
		Map<String, ResourcePartner> updated = new HashMap<String, ResourcePartner>();

		for (String nuc : latest.keySet())
		{
			ResourcePartner l = latest.get(nuc);
			ResourcePartner p = partners.get(nuc);

			boolean requiresUpdate = false;

			if (!ObjectUtil.compareStrings(p.getEmailMain(), l.getEmailMain()))
			{
				changes.add(new ResourcePartnerChangeRecord(SOURCE_SYSTEM, nuc, "email_main", p.getEmailMain(),
				                                            l.getEmailMain()));
				p.setEmailMain(l.getEmailMain());
				requiresUpdate = true;
			}

			if (!ObjectUtil.compareStrings(p.getEmailIll(), l.getEmailIll()))
			{
				changes.add(new ResourcePartnerChangeRecord(SOURCE_SYSTEM, nuc, "email_ill", p.getEmailIll(),
				                                            l.getEmailIll()));
				p.setEmailIll(l.getEmailIll());
				requiresUpdate = true;
			}

			if (!ObjectUtil.compareStrings(p.getPhoneMain(), l.getPhoneMain()))
			{
				changes.add(new ResourcePartnerChangeRecord(SOURCE_SYSTEM, nuc, "phone_main", p.getPhoneMain(),
				                                            l.getPhoneMain()));
				p.setPhoneMain(l.getPhoneMain());
				requiresUpdate = true;
			}

			if (!ObjectUtil.compareStrings(p.getPhoneIll(), l.getPhoneIll()))
			{
				changes.add(new ResourcePartnerChangeRecord(SOURCE_SYSTEM, nuc, "phone_ill", p.getPhoneIll(),
				                                            l.getPhoneIll()));
				p.setPhoneIll(l.getPhoneIll());
				requiresUpdate = true;
			}

			if (!ObjectUtil.compareStrings(p.getPhoneFax(), l.getPhoneFax()))
			{
				changes.add(new ResourcePartnerChangeRecord(SOURCE_SYSTEM, nuc, "phone_fax", p.getPhoneFax(),
				                                            l.getPhoneFax()));
				p.setPhoneFax(l.getPhoneFax());
				requiresUpdate = true;
			}

			Map<String, ResourcePartnerAddress> pAddresses = new HashMap<String, ResourcePartnerAddress>();
			for (ResourcePartnerAddress ea : p.getAddresses())
				pAddresses.put(ea.getAddressType(), ea);

			for (ResourcePartnerAddress la : l.getAddresses())
			{
				ResourcePartnerAddress pa = pAddresses.remove(la.getAddressType());
				try
				{
					if (pa == null)
					{
						changes.add(new ResourcePartnerChangeRecord(SOURCE_SYSTEM, nuc,
						                                            "address:" + la.getAddressType(), null,
						                                            om.writeValueAsString(la)));
						p.getAddresses().add(la);
						requiresUpdate = true;
					}
					else if (!la.equals(pa))
					{
						changes.add(new ResourcePartnerChangeRecord(SOURCE_SYSTEM, nuc,
						                                            "address:" + la.getAddressType(),
						                                            om.writeValueAsString(pa),
						                                            om.writeValueAsString(la)));
						p.getAddresses().remove(pa);
						p.getAddresses().add(la);
						requiresUpdate = true;
					}
				}
				catch (JsonProcessingException jpe)
				{
					log.error("{}", jpe.getMessage(), jpe);
				}
			}

			for (String type : pAddresses.keySet())
			{
				changes.add(new ResourcePartnerChangeRecord(SOURCE_SYSTEM, nuc, "address:" + type + ":status", "active",
				                                            "inactive"));
				pAddresses.get(type).setAddressStatus("inactive");
				requiresUpdate = true;
			}

			if (requiresUpdate)
			{
				p.setUpdated(sdf.format(new Date()));
				updated.put(nuc, p);
				try
				{
					log.debug("to be updated[{}]: {}", nuc, om.writeValueAsString(p));
				}
				catch (JsonProcessingException jpe)
				{
					log.error("{}", jpe.getMessage(), jpe);
				}
			}
		}

		return updated;
	}

	private boolean shouldHarvestRun()
	{
		int daysBetween = Integer.parseInt(config.get("days_between_update").orElse(DEFAULT_DAYS_BETWEEN));
		if (config.get("days_between_update").isEmpty())
			config.set("days_between_update", DEFAULT_DAYS_BETWEEN);

		Calendar lastRun = Calendar.getInstance();
		lastRun.set(2017, Calendar.JANUARY, 1);

		if (config.get("last_run").isPresent())
		{
			try
			{
				lastRun.setTime(sdf.parse(config.get("last_run").get()));
			}
			catch (ParseException pe)
			{
				lastRun.set(2017, Calendar.JANUARY, 1);
			}
		}

		Calendar checkRun = Calendar.getInstance();
		checkRun.add(Calendar.DAY_OF_MONTH, -daysBetween);

		return checkRun.after(lastRun);
	}

	private class Harvester implements Callable<String>
	{
		private String nuc;

		public Harvester(String nuc, AtomicInteger count)
		{
			this.nuc = nuc;
			int c = count.incrementAndGet();
			if (c % 100 == 0)
				log.info("addresses processed: {}", c);
		}

		public String call()
		{
			String page = null;

			try
			{
				page = ilrs.getPage(nuc);
			}
			catch (NotFoundException nfe)
			{
				log.warn("failed to obtain ilrs page [{}]: {}", nuc, nfe.getMessage());
			}
			catch (Exception e)
			{
				log.error("failed to obtain ilrs page: {}", e.getMessage());
			}

			return page;
		}
	}
}
