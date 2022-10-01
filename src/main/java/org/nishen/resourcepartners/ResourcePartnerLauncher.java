package org.nishen.resourcepartners;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author nishen
 * 
 */
public class ResourcePartnerLauncher
{
	private static final Logger log = LoggerFactory.getLogger(ResourcePartnerLauncher.class);

	private static Map<String, String> options;

	public static final Set<String> ACTIONS = Set.of("harvest", "sync", "preview");

	public static void main(String[] args)
	{
		try
		{
			options = parseArgs(args);

			if (options.containsKey("h"))
			{
				printUsage();
				return;
			}

			if (options.containsKey("harvesters"))
				System.setProperty("harvesters", options.get("harvesters"));
		}
		catch (Exception e)
		{
			log.error("invalid command line: {}", e.getMessage());
			return;
		}

		// create the injector
		log.debug("creating injector");
		Injector injector = Guice.createInjector(List.of(new ResourcePartnerModule()));

		// initialise the application object.
		log.debug("creating application");
		ResourcePartnerApp app = injector.getInstance(ResourcePartnerApp.class);

		// execute the application
		Calendar timeStart = Calendar.getInstance();
		log.info("executing: [{}]", timeStart.getTime());

		app.run(options);

		Calendar timeEnd = Calendar.getInstance();
		log.info("execution complete: [{}]", timeEnd.getTime());

		long diff = (timeEnd.getTimeInMillis() - timeStart.getTimeInMillis()) / 1000;
		log.info("time taken (seconds): {}", diff);
	}

	private static Map<String, String> parseArgs(String[] args) throws Exception
	{
		Map<String, String> options = new HashMap<String, String>();

		if (args.length == 0)
		{
			return options;
		}

		for (int x = 0; x < args.length; x++)
		{
			if (args[x].equals("-h"))
			{
				options.put("h", "true");
			}
			else if (args[x].equals("-?"))
			{
				options.put("h", "true");
			}
			else if (args[x].equals("-action"))
			{
				if (args.length > (x + 1))
				{
					String action = args[++x].toLowerCase();
					if (!ACTIONS.contains(action))
						throw new Exception("invalid action: " + action);
					options.put("action", action);
				}
				else
				{
					throw new Exception("you need to specify an action");
				}
			}
			else if (args[x].equals("-harvesters"))
			{
				if (args.length > (x + 1))
				{
					options.put("harvesters", args[++x].toUpperCase());
				}
				else
				{
					throw new Exception("list of harvesters required with -harvesters parameter.");
				}
			}
			else
			{
				throw new Exception("unknown option: " + args[x]);
			}
		}

		return options;
	}

	private static void printUsage()
	{
		System.out.println("java -jar resource-partner-sharing-x.y.z.jar [options]");
		System.out.println("  -h                                        help");
		System.out.println("  -?                                        help");
		System.out.println("  -action (harvest|sync|preview)");
		System.out.println("      harvest                               run the harvest");
		System.out.println("      sync                                  sync partner data with Alma");
		System.out.println("      preview                               show what would happen without updating Alma");
		System.out.println("  -harvesters harvester1,hearvester2,...    allows selection of harvesters to run by specifying");
		System.out.println("                                            a comma separated list: [LADD,ILRS,TEPUNA,OUTLOOK]");
		System.out.println("                                            (only works with the harvest action)");
	}
}
