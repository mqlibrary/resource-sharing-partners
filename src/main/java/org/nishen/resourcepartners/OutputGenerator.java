package org.nishen.resourcepartners;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.nishen.resourcepartners.entity.ResourcePartnerChangeRecord;
import org.nishen.resourcepartners.model.Partner;

public interface OutputGenerator
{
	public void savePartners(Map<String, Partner> changed) throws FileNotFoundException;

	public void saveDeleted(Map<String, Partner> deleted) throws FileNotFoundException;

	public void saveChanges(Map<String, List<ResourcePartnerChangeRecord>> changes) throws FileNotFoundException;

	public void saveHarvestChanges(List<ResourcePartnerChangeRecord> changes) throws FileNotFoundException;
}
