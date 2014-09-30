package it.cnr.isti.melampo.tools.thumbnail.locator;

import java.util.Map;


public interface ImageLocator {

	public static final String PROP_LOCATOR_REPOSITORY_URL = "thumbnail.locator.local.repository.url";
	//public static final String PROP_LOCAL_LOCATOR_PREFIX = "thumbnail.locator.local";
	
	/**
	 * generating the URL of the image with the thumbnailId from the given dataset managed by the given application 
	 * @param dataset
	 * @param thumbnailId
	 * @param applicationUrl
	 * @return
	 */
	public String getImageUrl(String dataset, String thumbnailId, String applicationUrl);
	
	/**
	 * generating the image url by using the current folder ("./" ) as base application folder 
	 * @param dataset
	 * @param thumbnailId
	 * @return
	 */
	public String getImageUrl(String dataset, String thumbnailId);
	
	/**
	 * the parameters used for configuring the image locator
	 * @param params
	 */
	public void setConfigParams(Map<String, String> params);
}
