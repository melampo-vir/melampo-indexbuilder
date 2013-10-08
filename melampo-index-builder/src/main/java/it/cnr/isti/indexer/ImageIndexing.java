package it.cnr.isti.indexer;

import it.cnr.isti.config.index.IndexConfiguration;
import it.cnr.isti.config.index.IndexConfigurationImpl;
import it.cnr.isti.exception.ImageIndexingException;
import it.cnr.isti.feature.extraction.FeatureExtractionException;
import it.cnr.isti.feature.extraction.Image2Features;
import it.cnr.isti.melampo.index.indexing.LireIndexer;
import it.cnr.isti.melampo.index.settings.LireSettings;
import it.cnr.isti.vir.features.FeaturesCollectorArr;
import it.cnr.isti.vir.features.mpeg7.LireObject;
import it.cnr.isti.vir.id.IDString;
import it.cnr.isti.vir.readers.CoPhIRv2Reader;
import it.cnr.isti.vir.similarity.metric.LireMetric;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.lucene.index.CorruptIndexException;


public class ImageIndexing  {

	private LireIndexer mp7cIndex;

	private LireSettings settings;
	private Image2Features img2Features;
	private IndexConfiguration configuration;
	private String dataset;
	
//	@Deprecated
//	public ImageIndexing(File confDir) {
//		this.confDir = confDir;
//	}

	public ImageIndexing(String dataset, IndexConfiguration indexConfig) {
		this.dataset = dataset;
		if(indexConfig == null)
			configuration = new IndexConfigurationImpl();
		else configuration = indexConfig;
		if(dataset == null)
			this.dataset = getConfiguration().getDefaultDataset();
	}
	
	public void openIndex() throws ImageIndexingException {
		CoPhIRv2Reader.setFeatures(LireMetric.reqFeatures);
		//TODO: implement index initialization
		
		try {
			File confDir = getConfiguration().getIndexConfFolder(getDataset());
			img2Features = new Image2Features(confDir);
			//new LireSettings(new File(confDir, "LIRE_MP7ALL.properties"));
			settings = getConfiguration().getLireSettings(getDataset()); 
			// coll = settings.getFCArchives().getArchive(0);
			mp7cIndex = new LireIndexer();
			mp7cIndex.OpenIndex(settings);
	
		} catch (Exception e) {
			throw new ImageIndexingException("Error opening the image index ",
					e);
		}
	}

	public void closeIndex() throws ImageIndexingException {
		try {
			mp7cIndex.closeIndex();
		} catch (Exception e) {
			throw new ImageIndexingException("Error closing the image index ",
					e);
		}
	}

	public void insertImage(String docID, URL imageURL)
			throws ImageIndexingException, FeatureExtractionException {

		String imgFeatures = img2Features.extractFeatures(imageURL);
		InputStream is = new ByteArrayInputStream(imgFeatures.getBytes());

		// read it with BufferedReader
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		try {
			FeaturesCollectorArr features = CoPhIRv2Reader.getObj(br);
			features.setID(new IDString(docID));
			// coll.add(features);
			LireObject obj = new LireObject(features);
			mp7cIndex.addDocument(obj, docID);
			
		} catch (Exception e) {
			throw new ImageIndexingException("Error indexing " + docID, e);
		}

	}

	public void insertImage(String docID, InputStream imageObj)
			throws ImageIndexingException, FeatureExtractionException {

		String imgFeatures = img2Features.extractFeatures(imageObj);
		InputStream is = new ByteArrayInputStream(imgFeatures.getBytes());

		// read it with BufferedReader
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		FeaturesCollectorArr features;
		try {
			features = CoPhIRv2Reader.getObj(br);
			features.setID(new IDString(docID));
			LireObject obj = new LireObject(features);
			mp7cIndex.addDocument(obj, docID);
		} catch (Exception e) {
			throw new ImageIndexingException("Error indexing " + docID, e);
		}

		// coll.add(features);
	}

	public void deleteImage(String docID) throws ImageIndexingException {
		try {
			mp7cIndex.deleteDocument(docID);
		} catch (Exception e) {
			throw new ImageIndexingException(
					"Error, unable to delete document " + docID, e);
		}
	}

	public void updateImage(String docID, InputStream imageObj)
			throws ImageIndexingException, FeatureExtractionException {

		String imgFeatures = img2Features.extractFeatures(imageObj);
		InputStream is = new ByteArrayInputStream(imgFeatures.getBytes());

		// read it with BufferedReader
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		FeaturesCollectorArr features;
		try {
			features = CoPhIRv2Reader.getObj(br);
			features.setID(new IDString(docID));
			LireObject obj = new LireObject(features);
			mp7cIndex.updateDocument(obj, docID);
		} catch (Exception e) {
			throw new ImageIndexingException("Error indexing " + docID, e);
		}

	}

	public void updateImage(String docID, URL imageURL)
			throws ImageIndexingException, FeatureExtractionException {
		String imgFeatures = img2Features.extractFeatures(imageURL);
		InputStream is = new ByteArrayInputStream(imgFeatures.getBytes());

		// read it with BufferedReader
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		try {
			FeaturesCollectorArr features = CoPhIRv2Reader.getObj(br);
			features.setID(new IDString(docID));
			LireObject obj = new LireObject(features);
			mp7cIndex.updateDocument(obj, docID);
		} catch (Exception e) {
			throw new ImageIndexingException("Error indexing " + docID, e);
		}

	}

	public void optimizeIndex() throws ImageIndexingException {
		try {
			mp7cIndex.optimizeIndex();
		} catch (CorruptIndexException e) {
			throw new ImageIndexingException("Error optimizing index ", e);
		} catch (IOException e) {
			throw new ImageIndexingException("Error optimizing index ", e);
		}

	}

	public IndexConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(IndexConfiguration indexConf) {
		this.configuration = indexConf;
	}

	public String getDataset() {
		return dataset;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

}

