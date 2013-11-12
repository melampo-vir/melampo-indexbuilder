package it.cnr.isti.config.index;

import it.cnr.isti.config.Configuration;
import it.cnr.isti.melampo.index.settings.LireSettings;
import it.cnr.isti.melampo.vir.exceptions.VIRException;

import java.io.File;
import java.io.IOException;

public interface IndexConfiguration extends Configuration{

	public File getFeaturesArchiveFile(String dataset);

	public LireSettings getLireSettings(String dataset) throws IOException,
			VIRException;

	public String getDefaultDataset();

	public File getImageFxFile(String dataset);

	public File getIndexConfFolder(String dataset);

	public File getIndexFolder(String dataset);

	public File getIndexImagesFolder(String dataset);
	
	public File getDatasetsFolder();

	public File getDatasetFile(String dataset);
	
	

}