package org.nishen.resourcepartners;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.nishen.resourcepartners.entity.ResourcePartnerChangeRecord;

public interface ResourcePartnerHarvester
{
	public Optional<List<ResourcePartnerChangeRecord>> process(Map<String, String> options) throws Exception;
}
