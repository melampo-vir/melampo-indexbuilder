package it.cnr.isti.config;

import it.cnr.isti.exception.TechnicalRuntimeException;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * This class supports properties evaluation from configuration file.
 * 
 * @author GordeaS
 * @author GrafR
 */
public abstract class BaseConfiguration implements Configuration {

	protected static final String LIRE_SETTINGS = "LIRE_MP7ALL.properties";
	protected static final String FEATURES_ARCHIVE_FILE = "FCArchive-lire.dat";
	protected static final String IMAGE_FX_FILE = "image-fx.properties";
	protected static final String CONF_FOLDER = "conf";
	protected static final String DATASETS_FOLDER = "datasets";
	protected static final String IMAGES_FOLDER = "image";
	
	protected static final String PROP_IMAGE_INDEX_HOME = "image.index.home";
	protected static final String PROP_DATA_SET_DEFAULT = "dataset.default";
	
	protected Logger log = Logger.getLogger(getClass());
	private Properties properties = new Properties();
	private static final String CONFIG_FILE_PREFIX = "/";
	private static final String CONFIG_FILE_EXT = ".properties";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.assets.Configuration#getConfigProperty(java.lang.String)
	 */
	protected String getConfigProperty(String propertyName) {
		return (String) getProperties().get(propertyName);
	}

	protected BaseConfiguration() {
		log.trace("Initializing configuration object: " + getClass());
		init();
	}

	/**
	 * This method evaluates property from configuration file.
	 * 
	 * @param propertyName
	 * @param defaultValue
	 * @return property from configuration file or default value
	 */
	public String getConfigProperty(String propertyName, String defaultValue) {
		String ret = getConfigProperty(propertyName);
		return (ret != null) ? ret : defaultValue;
	}

//	/**
//	 * @return server URL from configuration file or default value
//	 */
//	public String getServerUrl() {
//		String res = getConfigProperty(PROP_SERVER_URL);
//		return (res != null) ? res : DEFAULT_SERVER_URL;
//	}

//	/**
//	 * @return application name from configuration file or default value
//	 */
//	public String getApplicationName() {
//		String res = getConfigProperty(PROP_APPLICATION_NAME);
//		return (res != null) ? res : DEFAULT_APPLICATION_NAME;
//	}

	/**
	 * @return default service URL
	 */
//TODO: remove legacy code
	//	public String getServiceUrl() {
//		return getServiceUrl(null, null);
//	}

	/**
	 * This method creates URL to the rest service for particular component.
	 * 
	 * @param componentName
	 * @param serviceName
	 * @return URL to the rest service for particular component
	 */
	//TODO: remove legacy code
//	public String getServiceUrl(String componentName, String serviceName) {
//		String res = getServerUrl() + PATH_SEPARATOR + getApplicationName();
//
//		if (componentName != null)
//			res += (PATH_SEPARATOR + componentName);
//		else
//			res += (PATH_SEPARATOR + getComponentName());
//
//		if (serviceName != null)
//			res += (PATH_SEPARATOR + serviceName);
//
//		return (res + "/rest");
//	}

	/**
	 * Method used for initialization of this configuration object
	 */
	public void init() {
		loadProperties();
	}

	/**
	 * This method reads a configuration file and retrieves properties.
	 */
	protected void loadProperties() {

		try {
			InputStream resourceAsStream = getClass().getResourceAsStream(
					getConfigFile());
			if (resourceAsStream != null)
				getProperties().load(resourceAsStream);
			else
				log.info("No configuration file found for component: "
						+ getComponentName());

		} catch (Exception e) {
			throw new TechnicalRuntimeException(
					"Cannot read configuration file: " + getConfigFile(), e);
		}

	}

	/**
	 * @return configuration file name containing configuration file prefix,
	 *         component name and file extension
	 */
	protected String getConfigFile() {
		return CONFIG_FILE_PREFIX + getComponentName() + CONFIG_FILE_EXT;
	}

	/**
	 * @return properties from configuration file
	 */
	public Properties getProperties() {
		return properties;
	}
}
