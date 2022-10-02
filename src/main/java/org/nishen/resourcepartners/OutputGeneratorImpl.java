package org.nishen.resourcepartners;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.nishen.resourcepartners.entity.ResourcePartnerChangeRecord;
import org.nishen.resourcepartners.model.ObjectFactory;
import org.nishen.resourcepartners.model.Partner;
import org.nishen.resourcepartners.model.Partners;
import org.nishen.resourcepartners.util.JaxbUtilModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class OutputGeneratorImpl implements OutputGenerator
{
	private static final Logger log = LoggerFactory.getLogger(OutputGenerator.class);

	private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	private static final LocalDateTime now = LocalDateTime.now();

	private static final ObjectFactory of = new ObjectFactory();

	private static final List<String> CHANGES_HEADERS = List.of("NUC", "TIME", "FIELD", "BEFORE", "AFTER");

	private File outputFolder;

	@Inject
	public OutputGeneratorImpl(@Named("location.output") String outputFolderName)
	{
		this.outputFolder = new File(outputFolderName);
		if (!outputFolder.isDirectory())
			outputFolder.mkdirs();
	}

	@Override
	public void savePartners(Map<String, Partner> changed) throws FileNotFoundException
	{
		Partners p = of.createPartners();
		p.setTotalRecordCount(changed.size());
		changed.keySet().stream().sorted().forEach(c -> p.getPartner().add(changed.get(c)));

		String filename = this.outputFolder.getAbsolutePath() + File.separatorChar + "partners-changed-" +
		                  format.format(now) + ".json";
		OutputStream outputStream = new FileOutputStream(new File(filename));

		JaxbUtilModel.formatPretty(p, outputStream);
		log.info("generated changed partner file: {}", filename);
	}

	@Override
	public void saveDeleted(Map<String, Partner> deleted) throws FileNotFoundException
	{
		Partners p = of.createPartners();
		p.setTotalRecordCount(deleted.size());
		deleted.keySet().stream().sorted().forEach(c -> p.getPartner().add(deleted.get(c)));

		String filename = this.outputFolder.getAbsolutePath() + File.separatorChar + "partners-deleted-" +
		                  format.format(now) + ".json";
		OutputStream outputStream = new FileOutputStream(new File(filename));

		JaxbUtilModel.formatPretty(p, outputStream);
		log.info("generated deleted partner file: {}", filename);
	}

	@Override
	public void saveChanges(Map<String, List<ResourcePartnerChangeRecord>> changes) throws FileNotFoundException
	{
		String filename = this.outputFolder.getAbsolutePath() + File.separatorChar + "partners-changes-" +
		                  format.format(now) + ".csv";

		try (CSVPrinter printer = new CSVPrinter(new FileWriter(filename), CSVFormat.EXCEL))
		{
			printer.printRecord(CHANGES_HEADERS);

			for (String nuc : changes.keySet())
				for (ResourcePartnerChangeRecord c : changes.get(nuc))
					printer.printRecord(c.getNuc(), c.getTime(), c.getField(), c.getBefore(), c.getAfter());
		}
		catch (IOException ioe)
		{
			log.error("{}", ioe.getMessage(), ioe);
		}

		log.info("generated field changes file: {}", filename);
	}

	@Override
	public void saveHarvestChanges(List<ResourcePartnerChangeRecord> changes) throws FileNotFoundException
	{
		String filename = this.outputFolder.getAbsolutePath() + File.separatorChar + "harvest-partners-changes-" +
		                  format.format(now) + ".csv";

		try (CSVPrinter printer = new CSVPrinter(new FileWriter(filename), CSVFormat.EXCEL))
		{
			printer.printRecord(CHANGES_HEADERS);

			for (ResourcePartnerChangeRecord c : changes)
				printer.printRecord(c.getNuc(), c.getTime(), c.getField(), c.getBefore(), c.getAfter());
		}
		catch (IOException ioe)
		{
			log.error("{}", ioe.getMessage(), ioe);
		}

		log.info("generated field changes file: {}", filename);
	}
}
