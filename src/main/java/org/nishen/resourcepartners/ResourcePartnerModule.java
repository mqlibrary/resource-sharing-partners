package org.nishen.resourcepartners;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.nishen.resourcepartners.dao.Config;
import org.nishen.resourcepartners.dao.ConfigFactory;
import org.nishen.resourcepartners.dao.ConfigImpl;
import org.nishen.resourcepartners.dao.DatastoreDAO;
import org.nishen.resourcepartners.dao.DatastoreDAOImpl;
import org.nishen.resourcepartners.dao.IlrsDAO;
import org.nishen.resourcepartners.dao.IlrsDAOImpl;
import org.nishen.resourcepartners.dao.LaddDAO;
import org.nishen.resourcepartners.dao.LaddDAOImpl;
import org.nishen.resourcepartners.dao.OutlookDAO;
import org.nishen.resourcepartners.dao.OutlookDAOImpl;
import org.nishen.resourcepartners.dao.TepunaDAO;
import org.nishen.resourcepartners.dao.TepunaDAOImpl;
import org.nishen.resourcepartners.harvesters.Harvester;
import org.nishen.resourcepartners.harvesters.HarvesterIlrs;
import org.nishen.resourcepartners.harvesters.HarvesterLadd;
import org.nishen.resourcepartners.harvesters.HarvesterTepuna;
import org.nishen.resourcepartners.harvesters.HarvesterTepunaStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class ResourcePartnerModule extends AbstractModule
{
	private static final Logger log = LoggerFactory.getLogger(ResourcePartnerModule.class);

	private static final String CONFIG_FILE = "app.properties";

	private static final Properties config = new Properties();

	private WebTarget ilrsTarget = null;

	private WebTarget laddTarget = null;

	private WebTarget tepunaTarget = null;

	private WebTarget outlookTarget = null;

	private WebTarget outlookTokenTarget = null;

	@Override
	protected void configure()
	{
		String configFilename = CONFIG_FILE;
		if (System.getProperty("config") != null)
			configFilename = System.getProperty("config");

		File configFile = new File(configFilename);
		try
		{
			if (!configFile.exists() || !configFile.canRead())
				throw new IOException("cannot read config file: " + configFile.getAbsolutePath());

			config.load(new FileReader(configFile));

			if (log.isDebugEnabled())
				for (String k : config.stringPropertyNames())
					log.debug("{}: {}={}", new Object[] { CONFIG_FILE, k, config.getProperty(k) });
		}
		catch (IOException e)
		{
			log.error("unable to load configuration: {}", configFile.getAbsoluteFile(), e);
			return;
		}

		String harvesters = System.getProperty("harvesters");
		Set<String> harvesterSet = null;
		if (harvesters != null)
			harvesterSet = new HashSet<String>(Arrays.asList(harvesters.toLowerCase().split(",")));

		// bind instances
		bind(ResourcePartnerProcessor.class).to(ResourcePartnerProcessorImpl.class);

		bind(DatastoreDAO.class).to(DatastoreDAOImpl.class);
		bind(IlrsDAO.class).to(IlrsDAOImpl.class);
		bind(LaddDAO.class).to(LaddDAOImpl.class);
		bind(TepunaDAO.class).to(TepunaDAOImpl.class);
		bind(OutlookDAO.class).to(OutlookDAOImpl.class);

		Multibinder<Harvester> harvesterBinder = Multibinder.newSetBinder(binder(), Harvester.class);
		if (harvesterSet == null || harvesterSet.contains("ladd"))
			harvesterBinder.addBinding().to(HarvesterLadd.class);

		if (harvesterSet == null || harvesterSet.contains("ilrs"))
			harvesterBinder.addBinding().to(HarvesterIlrs.class);

		if (harvesterSet == null || harvesterSet.contains("tepuna"))
			harvesterBinder.addBinding().to(HarvesterTepuna.class);

		if (harvesterSet == null || harvesterSet.contains("outlook"))
			harvesterBinder.addBinding().to(HarvesterTepunaStatus.class);

		install(new FactoryModuleBuilder().implement(Config.class, ConfigImpl.class).build(ConfigFactory.class));

		bind(String.class).annotatedWith(Names.named("location.config"))
		                  .toInstance(config.getProperty("location.config"));
		bind(String.class).annotatedWith(Names.named("location.partners"))
		                  .toInstance(config.getProperty("location.partners"));
		bind(String.class).annotatedWith(Names.named("outlook.client.email"))
		                  .toInstance(config.getProperty("outlook.client.email"));
		bind(String.class).annotatedWith(Names.named("outlook.client.id"))
		                  .toInstance(config.getProperty("outlook.client.id"));
		bind(String.class).annotatedWith(Names.named("outlook.client.secret"))
		                  .toInstance(config.getProperty("outlook.client.secret"));
	}

	@Provides
	@Named("ws.ilrs")
	protected WebTarget provideWebTargetIlrs()
	{
		if (ilrsTarget == null)
		{
			Client client = ClientBuilder.newClient();
			ilrsTarget = client.target(config.getProperty("ws.url.ilrs"));
		}

		return ilrsTarget;
	}

	@Provides
	@Named("ws.ladd")
	protected WebTarget provideWebTargetLadd() throws Exception
	{
		if (laddTarget == null)
		{
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, getTrustManager(), new java.security.SecureRandom());

			Client client =
			        ClientBuilder.newBuilder().sslContext(sslcontext).hostnameVerifier((s1, s2) -> true).build();
			laddTarget = client.target(config.getProperty("ws.url.ladd"));
		}

		return laddTarget;
	}

	@Provides
	@Named("ws.tepuna")
	protected WebTarget provideWebTargetTepuna()
	{
		if (tepunaTarget == null)
		{
			Client client = ClientBuilder.newClient();
			tepunaTarget = client.target(config.getProperty("ws.url.tepuna"));
		}

		return tepunaTarget;
	}

	@Provides
	@Named("ws.outlook")
	protected WebTarget provideWebTargetOutlook() throws Exception
	{
		if (outlookTarget == null)
		{
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, getTrustManager(), new java.security.SecureRandom());

			Client client =
			        ClientBuilder.newBuilder().sslContext(sslcontext).hostnameVerifier((s1, s2) -> true).build();
			outlookTarget = client.target(config.getProperty("outlook.url.endpoint"));
		}

		return outlookTarget;
	}

	@Provides
	@Named("ws.outlook.token")
	protected WebTarget provideWebTargetOutlookToken() throws Exception
	{
		if (outlookTokenTarget == null)
		{
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, getTrustManager(), new java.security.SecureRandom());

			Client client =
			        ClientBuilder.newBuilder().sslContext(sslcontext).hostnameVerifier((s1, s2) -> true).build();
			outlookTokenTarget = client.target(config.getProperty("outlook.url.token"));
		}

		return outlookTokenTarget;
	}

	private TrustManager[] getTrustManager()
	{
		return new TrustManager[] { new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
			{}

			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
			{}

			public X509Certificate[] getAcceptedIssuers()
			{
				return new X509Certificate[0];
			}
		} };
	}
}
