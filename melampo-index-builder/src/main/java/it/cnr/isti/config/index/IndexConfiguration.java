package it.cnr.isti.config.index;

import it.cnr.isti.config.Configuration;
import it.cnr.isti.melampo.index.settings.LireSettings;
import it.cnr.isti.melampo.vir.exceptions.VIRException;

import java.io.File;
import java.io.IOException;

public interface IndexConfiguration extends Configuration{

	final String DEFAULT_IMAGE_EXT = ".jpg";
	/**
	 * Returns the location of the file containing the archive with the extracted features
	 * @param dataset
	 * @return
	 */
	public File getFeaturesArchiveFile(String dataset);

	/**
	 * This methd loads the settings for the (LIRE based) image indexes using  
	 * @param dataset
	 * @return
	 * @throws IOException
	 * @throws VIRException
	 */
	public LireSettings getLireSettings(String dataset) throws IOException,
			VIRException;

	/**
	 * This method returns the name of the default dataset as defined in the configuration file
	 * @return the dataset name
	 *
	 */
	public String getDefaultDataset();

	/**
	 * Returns the location of the feature extraction configuration file for the given dataset
	 * @param dataset
	 * @return
	 */
	public File getImageFxFile(String dataset);

	/**
	 * Returns the location of the index configuration folder for the given dataset
	 * @param dataset
	 * @return
	 */
	public File getIndexConfFolder(String dataset);

	/**
	 * Returns the location of the image index folder for the given dataset 
	 * @param dataset
	 * @return
	 */
	public File getIndexFolder(String dataset);

	/**
	 * Returns the location of the folder containing the image files for the given dataset
	 * @param dataset
	 * @return
	 */
	public File getImageFolderAsFile(String dataset);
	/**
	 * Returns the location of the file containing the thumbnail of the object identified by the given objectId, within the given dataset
	 * @param dataset - the name of the dataset
	 * @param objectId - the object id (e.g. europeanaID)
	 * @return
	 */
	public File getImageFile(String dataset, String objectId);
	
	/**
	 * Returns the location of the home folder for the default dataset
	 * @return
	 */
	public File getDatasetsFolderAsFile();

	/**
	 * Returns the location of the file containing the objectIDs and thumbnailUrls for the given dataset  
	 * @param dataset
	 * @return
	 */
	public File getDatasetFile(String dataset);
	
	
	/**
	 * returns the full classname of the imageLocator class
	 * @return
	 */
	public abstract String getImageLocatorClass();
	

}