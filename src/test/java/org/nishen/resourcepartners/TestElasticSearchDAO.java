package org.nishen.resourcepartners;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.nishen.resourcepartners.dao.ElasticSearchDAO;
import org.nishen.resourcepartners.entity.ElasticSearchPartner;
import org.nishen.resourcepartners.entity.ElasticSearchPartnerAddress;
import org.nishen.resourcepartners.entity.ElasticSearchSuspension;
import org.nishen.resourcepartners.model.Address;
import org.nishen.resourcepartners.model.Address.Country;
import org.nishen.resourcepartners.util.DataUtils;
import org.nishen.resourcepartners.util.JaxbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import static org.junit.Assert.fail;

public class TestElasticSearchDAO
{
	private static final Logger log = LoggerFactory.getLogger(TestElasticSearchDAO.class);

	private static Injector injector = null;

	private static ElasticSearchDAO elastic = null;

	@BeforeClass
	public static void setup()
	{
		// list for injector modules
		List<Module> modules = new ArrayList<Module>();

		// module (main configuration)
		modules.add(new ResourcePartnerModule());

		// create the injector
		log.debug("creating injector");
		injector = Guice.createInjector(modules);

		elastic = injector.getInstance(ElasticSearchDAO.class);
	}

	@Test
	public void testGetPartner()
	{
		log.debug("running test: {}", Arrays.asList(new Throwable().getStackTrace()).get(0).getMethodName());
		try
		{
			// add data
			Calendar c = Calendar.getInstance();
			c.clear();
			c.set(Calendar.YEAR, 2017);
			c.set(Calendar.MONTH, Calendar.JULY);
			c.set(Calendar.DAY_OF_MONTH, 21);

			Country country = new Country();
			country.setDesc("Australia");
			country.setValue("AUS");

			Address a = new Address();
			a.setLine1("101 Test Street");
			a.setLine2("Test Area");
			a.setCity("Testville");
			a.setPostalCode("5555");
			a.setCountry(country);

			ElasticSearchPartnerAddress ea = new ElasticSearchPartnerAddress();
			ea.setAddressType("main");
			ea.setAddressDetail(a);

			ElasticSearchPartner expected = new ElasticSearchPartner();
			expected.setNuc("TEST");
			expected.setName("Test Organisation");

			ElasticSearchSuspension suspension = new ElasticSearchSuspension();
			suspension.setSuspensionStatus(ElasticSearchSuspension.NOT_SUSPENDED);
			suspension.setSuspensionStart(null);
			suspension.setSuspensionEnd(null);

			expected.getSuspensions().add(suspension);

			elastic.addEntity(expected);

			ElasticSearchPartner actual = elastic.getPartner("TEST").get();
			log.debug("{}", actual.toString());
			assertThat(actual, equalTo(expected));

			elastic.delEntity(expected);
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetPartnerDoesNotExist()
	{
		log.debug("running test: {}", Arrays.asList(new Throwable().getStackTrace()).get(0).getMethodName());

		try
		{
			ElasticSearchPartner partner = elastic.getPartner("TEST").orElse(null);
			assertThat(partner, equalTo(null));
		}
		catch (IOException ioe)
		{
			fail(ioe.getMessage());
		}
	}

	@Test
	public void testGetPartners()
	{
		log.debug("running test: {}", Arrays.asList(new Throwable().getStackTrace()).get(0).getMethodName());
		try
		{
			Map<String, ElasticSearchPartner> p = elastic.getPartners();

			for (String nuc : p.keySet())
				log.debug("{}:\n{}", nuc, JaxbUtil.format(p.get(nuc)));

			assertThat(p.size(), greaterThan(0));
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetUnmarshalledElasticPartner()
	{
		log.debug("running test: {}", Arrays.asList(new Throwable().getStackTrace()).get(0).getMethodName());
		try
		{
			String datafile = "target/test-classes/data/elastic-partner.json";
			byte[] data = DataUtils.loadFile(datafile);

			String json = new String(data, "UTF-8");
			log.debug("json:\n{}", json);
			ElasticSearchPartner e = JaxbUtil.get(json, ElasticSearchPartner.class);
			log.debug("unmarshalled:\n{}", e);
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}
}
