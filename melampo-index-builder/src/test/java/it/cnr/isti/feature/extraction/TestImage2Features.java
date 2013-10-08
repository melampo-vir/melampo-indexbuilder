package it.cnr.isti.feature.extraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import it.cnr.isti.config.index.IndexConfiguration;
import it.cnr.isti.config.index.IndexConfigurationImpl;
import it.cnr.isti.indexer.BaseIndexingTest;
import it.cnr.isti.indexer.ImageIndexing;
import it.cnr.isti.vir.features.FeaturesCollectorArr;
import it.cnr.isti.vir.features.FeaturesCollectorException;
import it.cnr.isti.vir.features.mpeg7.vd.MPEG7VDFormatException;
import it.cnr.isti.vir.readers.CoPhIRv2Reader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;

public class TestImage2Features extends BaseIndexingTest {

	@Test
	public void testFeatureExtractionForAll() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, FeatureExtractionException, FactoryConfigurationError, MPEG7VDFormatException, XMLStreamException, InvocationTargetException, NoSuchMethodException, FeaturesCollectorException {
		//get all test images 
		File imageFolder = new File("./src/test/resources/images");
		String[] images = imageFolder.list();
		//check if the images were correctly read (by now 10 images)
		assertTrue(images.length >= 10);
		
		File currentImage = null;  
		double totalTime = 0;
		double extractionTime = 0;
		//extract features and compute total extraction time
		for (int i = 0; i < images.length; i++) {
			 currentImage = new File(imageFolder, images[i]);
			 extractionTime = extractAndSaveFeatures(currentImage);
			 //check if the feature extraction was successful
			 assertTrue(extractionTime > 0);
			 totalTime += extractionTime;
		}

		System.out.println("Average feature extraction effort: " + totalTime/images.length);
		
	}

	@Test
	public void testFeatureExtraction() throws IOException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException, FeatureExtractionException,
			IllegalArgumentException, SecurityException,
			FactoryConfigurationError, MPEG7VDFormatException,
			XMLStreamException, InvocationTargetException,
			NoSuchMethodException, FeaturesCollectorException {

		// extract features
		File image = new File(
				"./src/test/resources/images/cluj_avram_iancu.jpg");
		extractAndSaveFeatures(image);

	}

	protected double extractAndSaveFeatures(File image) throws IOException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException, FeatureExtractionException,
			FactoryConfigurationError, MPEG7VDFormatException,
			XMLStreamException, InvocationTargetException,
			NoSuchMethodException, FeaturesCollectorException {
		
		IndexConfiguration configuration = new IndexConfigurationImpl();
		
		Image2Features featureExtractor = new Image2Features(configuration.getIndexConfFolder(TEST_DATASET));

		long start = System.currentTimeMillis();
		String xmlFeatures = featureExtractor.extractFeatures(image);

		// log time
		double secs = ((double)System.currentTimeMillis() - start) / 1000;
		System.out.println("Time to exract features: " + secs + " for file: " + image.getCanonicalPath());

		// check feature extraction
		assertNotNull(xmlFeatures);

		FeaturesCollectorArr featureCollector = CoPhIRv2Reader
				.getObj(new BufferedReader(new StringReader(xmlFeatures)));

		// check feature parsing
		assertEquals(featureExtractor.lireExtractors.length, featureCollector
				.getFeatures().size());

		// generate file path
		final int endIndex = image.getName().length() - ".jpg".length();
		String fileName = image.getName().substring(0, endIndex);
		File featuresFile = new File("./src/test/resources/features/"
				+ fileName + ".xml");

		// write to file
		BufferedWriter writer = new BufferedWriter(new FileWriter(featuresFile));
		writer.write(xmlFeatures);
		writer.close();
		
		return secs;
	}
}
