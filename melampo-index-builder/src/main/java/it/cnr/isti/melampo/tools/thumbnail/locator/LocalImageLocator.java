package it.cnr.isti.melampo.tools.thumbnail.locator;

import java.util.HashMap;
import java.util.Map;


public class LocalImageLocator implements ImageLocator{

	Map<String, String> configParams = new HashMap<String, String>();
	
	public Map<String, String> getConfigParams() {
		return configParams;
	}

	@Override
	public synchronized String getImageUrl(String dataset, String thumbnailId, String applicationUrl) {

		StringBuilder imageUrl = new StringBuilder(applicationUrl);
		if(!applicationUrl.endsWith("/"))
			imageUrl.append("/");
		
		imageUrl.append("datasets/");
		imageUrl.append(dataset);
		imageUrl.append("/image");
		imageUrl.append(thumbnailId).append(".jpg");
		
		return imageUrl.toString();
	}

	@Override
	public String getImageUrl(String dataset, String thumbnailId) {
		return getImageUrl(dataset, thumbnailId, getRepositoryUrl());
	}

	/*
	 * (non-Javadoc)
	 * @see it.cnr.isti.melampo.tools.thumbnail.locator.ImageLocator#setConfigParams(java.util.Map)
	 */
	@Override
	public void setConfigParams(Map<String, String> params) {
		configParams.putAll(params);
	}

	protected String getRepositoryUrl(){
		return getConfigParams().get(PROP_LOCATOR_REPOSITORY_URL);
	}
}
