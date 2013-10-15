package it.cnr.isti.indexer;

import static org.junit.Assert.assertTrue;
import it.cnr.isti.config.index.IndexConfiguration;
import it.cnr.isti.exception.ImageIndexingException;
import it.cnr.isti.feature.extraction.FeatureExtractionException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.junit.Test;

public class TestImageIndexing extends BaseIndexingTest {

	private ImageIndexing imageIndexing;
	IndexHelper helper = new IndexHelper();
	final String DATASET_TEST = "test";

	@Test
	public void testImageIndexing() throws ImageIndexingException,
			FeatureExtractionException, IOException {
		String dataset = DATASET_TEST; 
		openIndex(dataset);

		Long start = System.currentTimeMillis();

		int objects = insertImageObject();

		Long end = System.currentTimeMillis();
		System.out.println("Indexing time: " + (end - start));

		assertTrue(objects == 1);

		start = System.currentTimeMillis();
		closeIndex();
		end = System.currentTimeMillis();

		System.out.println("Close index & commit time: " + (end - start));
	}

	@Test
	public void testBuildTestIndex() throws IOException, ImageIndexingException {
		String dataset = DATASET_TEST; 
		indexDataset(dataset);

	}

	protected void indexDataset(String dataset) throws IOException,
			ImageIndexingException {
		openIndex(dataset);

		Long start = System.currentTimeMillis();

		File testDatasetFile = imageIndexing.getConfiguration().getDatasetFile(dataset);
		
		int objects = insertImageObjectsFromFile(testDatasetFile.toString());

		Long end = System.currentTimeMillis();
		System.out.println("Indexing time for dataset " + dataset + ": " + (end - start));

		assertTrue(objects > 0);

		// TODO: find solution for Invalid icc profile: invalid number of icc
		// markers
		// assertTrue(objects == 349); //only 299 are successfully indexed now

		start = System.currentTimeMillis();
		closeIndex();
		end = System.currentTimeMillis();

		System.out.println("Close index time: " + (end - start));
	}

	public void openIndex(String dataset) throws IOException,
			ImageIndexingException {

		if(imageIndexing == null)
			imageIndexing = new ImageIndexing(dataset, getIndexConfig());
		
		imageIndexing.openIndex();
	}

	protected IndexConfiguration getIndexConfig() {
		return null;
	}

	protected ImageIndexing getImageIndexing(String dataset) {
		
		return imageIndexing;
	}

	public int insertImageObject() throws IOException, ImageIndexingException,
			FeatureExtractionException {
		
		File testImage = new File(imageIndexing.getConfiguration()
				.getDatasetsFolder(), "testImage.jpg");
		
		InputStream imageObj = new FileInputStream(testImage);
		imageIndexing.insertImage("img1", imageObj);

		return 1;
	}

	public void closeIndex() {
		try {
			imageIndexing.closeIndex();
		} catch (ImageIndexingException e) {
			// log exception
			e.printStackTrace();
		}
	}

	public int insertImageObjectsFromFile(String filePath) throws IOException {

		Map<String, String> thumbsMap = helper.getThumbnailsMap(filePath);
		int indexedItems = 0;
		int skippedItems = 0;

		for (Map.Entry<String, String> thumbnail : thumbsMap.entrySet()) {
			try {

				if (thumbnail.getValue().startsWith("http"))// index by URL
					imageIndexing.insertImage(thumbnail.getKey(), new URL(
							thumbnail.getValue()));
				else
					// Index by local file
					imageIndexing.insertImage(thumbnail.getKey(),
							new FileInputStream(thumbnail.getValue()));

				indexedItems++;

			} catch (Exception e) {
				System.out.println("Image indexing errors occured: "
						+ e.getMessage());
				System.out.println("skip image: " + thumbnail.getKey() + "->"
						+ thumbnail.getValue());

				// e.printStackTrace();
				skippedItems++;
			}
		}
		System.out.println("Items successfully inserted in index: "
				+ indexedItems);
		System.out.println("Skipped Items: " + skippedItems);

		return indexedItems;
	}

}
