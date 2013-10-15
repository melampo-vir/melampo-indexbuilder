package it.cnr.isti.config.index;

import java.io.File;
import java.io.IOException;

import it.cnr.isti.config.BaseConfiguration;
import it.cnr.isti.melampo.index.settings.LireSettings;
import it.cnr.isti.melampo.vir.exceptions.VIRException;

public class IndexConfigurationImpl extends BaseConfiguration implements IndexConfiguration {

	File indexHome;
	LireSettings lireSettings;
	File featuresArchiveFile = null;
	
	@Override
	public String getComponentName() {
		return "index-builder";
	}

	protected File getIndexHomeFolder() {
		if (indexHome == null) {
			indexHome = new File(getConfigProperty(PROP_IMAGE_INDEX_HOME));
		}
		return indexHome;
	}

	@Override
	public File getIndexFolder(String dataset) {
		if (dataset != null)
			return new File(getIndexHomeFolder(), dataset);
		else
			return new File(getIndexHomeFolder(), getDefaultDataset());
	}

	@Override
	public File getDatasetsFolder() {
		return new File(getIndexHomeFolder(), DATASETS_FOLDER);
	}
	
	@Override
	public File getDatasetFile(String dataset) {
		if(dataset == null)
			return new File(getDatasetsFolder(), getDefaultDataset() + ".csv");
		else
			return new File(getDatasetsFolder(), dataset+".csv");
	}
	
	
	@Override
	public File getIndexConfFolder(String dataset) {
		return new File(getIndexFolder(dataset), CONF_FOLDER);
	}

	@Override
	public File getImageFxFile(String dataset) {
	
		return new File(getIndexConfFolder(dataset),
				IMAGE_FX_FILE);
	}

	@Override
	public String getDefaultDataset() {
		return getConfigProperty(PROP_DATA_SET_DEFAULT);
	}

	public IndexConfigurationImpl() {
		super();
	}
	
	@Override
	public LireSettings getLireSettings(String dataset) throws IOException, VIRException {
		if (lireSettings == null)
			lireSettings = new LireSettings(new File(getIndexConfFolder(dataset),
					LIRE_SETTINGS));
		return lireSettings;
	}

	@Override
	public File getFeaturesArchiveFile(String dataset) {

		return new File(getIndexFolder(dataset),
				FEATURES_ARCHIVE_FILE);
	}

}
