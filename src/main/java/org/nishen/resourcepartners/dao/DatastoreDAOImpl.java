package org.nishen.resourcepartners.dao;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.nishen.resourcepartners.entity.BaseEntity;
import org.nishen.resourcepartners.entity.ResourcePartner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DatastoreDAOImpl implements DatastoreDAO
{
	private static final Logger log = LoggerFactory.getLogger(DatastoreDAOImpl.class);

	private ObjectMapper om;

	private String dataFolder;

	private String overrideFolder;

	private String previousFolder;

	@Inject
	public DatastoreDAOImpl(@Named("location.partners") String dataFolder)
	{
		this.dataFolder = dataFolder;
		File df = new File(this.dataFolder);
		if (!df.isDirectory())
			df.mkdirs();

		this.overrideFolder = this.dataFolder + File.separatorChar + "override";
		File of = new File(this.overrideFolder);
		if (!of.isDirectory())
			of.mkdirs();

		this.previousFolder = this.dataFolder + File.separatorChar + "previous";
		File pf = new File(this.previousFolder);
		if (!pf.isDirectory())
			pf.mkdirs();

		this.om = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
	}

	@Override
	public Optional<ResourcePartner> getPartner(String id) throws IOException
	{
		String filename = overrideFolder + File.separatorChar + makeFilenameSafe(id).toUpperCase() + ".json";
		File file = new File(filename);
		if (!file.canRead())
		{
			filename = dataFolder + File.separatorChar + makeFilenameSafe(id).toUpperCase() + ".json";
			file = new File(filename);
		}

		if (!file.canRead())
			return Optional.empty();

		log.debug("getting partner: {}", filename);
		ResourcePartner p = om.readValue(file, ResourcePartner.class);

		return Optional.ofNullable(p);
	}

	@Override
	public Map<String, ResourcePartner> getPartners() throws IOException
	{
		File folder = new File(this.dataFolder);

		Map<String, ResourcePartner> partners = new HashMap<>();
		for (File f : folder.listFiles())
		{
			if (!f.getName().toLowerCase().endsWith(".json"))
				continue;

			String id = makeNucFromFilename(f.getName()).replace(".json", "");
			getPartner(id).ifPresent(p -> partners.put(id, p));
		}

		folder = new File(this.overrideFolder);
		for (File f : folder.listFiles())
		{
			if (!f.getName().toLowerCase().endsWith(".json"))
				continue;

			String id = makeNucFromFilename(f.getName()).replace(".json", "");
			getPartner(id).ifPresent(p -> partners.put(id, p));
		}

		return partners;
	}

	@Override
	public void addEntity(BaseEntity esEntity) throws IOException
	{
		String previousFilename =
		        this.previousFolder + File.separatorChar + makeFilenameSafe(esEntity.getEntityId()) + ".json";
		File previous = new File(previousFilename);
		if (previous.canWrite())
			previous.delete();

		String partnerFilename =
		        this.dataFolder + File.separatorChar + makeFilenameSafe(esEntity.getEntityId()) + ".json";
		File entity = new File(partnerFilename);
		if (entity.canWrite())
			entity.renameTo(previous);

		om.writeValue(entity, esEntity);
	}

	@Override
	public void addEntities(Collection<? extends BaseEntity> esEntities) throws IOException
	{
		for (BaseEntity e : esEntities)
			addEntity(e);
	}

	@Override
	public void delEntity(BaseEntity esEntity) throws IOException
	{
		String filename = this.dataFolder + File.separatorChar + makeFilenameSafe(esEntity.getEntityId()) + ".json";
		File f = new File(filename);
		f.delete();
	}

	@Override
	public void delEntities(Collection<? extends BaseEntity> esEntities) throws IOException
	{
		for (BaseEntity e : esEntities)
			delEntity(e);
	}

	private static String makeFilenameSafe(String s)
	{
		return s.replace(':', '_');
	}

	private static String makeNucFromFilename(String s)
	{
		return s.replace('_', ':');
	}
}
