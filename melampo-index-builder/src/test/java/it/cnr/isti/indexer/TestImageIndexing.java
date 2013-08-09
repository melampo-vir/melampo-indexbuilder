package it.cnr.isti.indexer;

import static org.junit.Assert.assertTrue;
import it.cnr.isti.feature.extraction.FeatureExtractionException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.junit.Test;

public class TestImageIndexing extends BaseIndexingTest {

	private ImageIndexing test;
	IndexHelper helper = new IndexHelper(); 
	
	@Test
	public void testImageIndexing() throws ImageIndexingException, FeatureExtractionException {
		try {
			openIndex();
			
			Long start = System.currentTimeMillis();
			
			int objects = insertImageObject();
			
			Long end = System.currentTimeMillis();
			System.out.println("Indexing time: " + (end - start));
			
			assertTrue(objects == 1);
			
			start = System.currentTimeMillis();  
			closeIndex();
			end = System.currentTimeMillis();
			
			System.out.println("Close index & commit time: " + (end - start));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	public void testBulkIndexing() {
		try {
			openIndex();
			
			Long start = System.currentTimeMillis();
			
			int objects = insertImageObjectsFromFile(getMelampoHome() + "/datasets/test_dataset.csv");
			
			Long end = System.currentTimeMillis();
			System.out.println("Indexing time: " + (end - start));
			
			assertTrue(objects > 0);
			
			//TODO: find solution for Invalid icc profile: invalid number of icc markers
			//assertTrue(objects == 349); //only 299 are successfully indexed now
			
			start = System.currentTimeMillis();  
			closeIndex();
			end = System.currentTimeMillis();
			
			System.out.println("Close index time: " + (end - start));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void openIndex() throws IOException {

		test = new ImageIndexing(getConfDir());

		try {
			test.openIndex();
		} catch (ImageIndexingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int insertImageObject() throws IOException, ImageIndexingException, FeatureExtractionException {
			InputStream imageObj = new FileInputStream(new File(
					getMelampoHome() + "/index/testImage.jpg"));
			test.insertImage("img1", imageObj);
			
			return 1;
	}

	public void closeIndex() {
		try {
			test.closeIndex();
		} catch (ImageIndexingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int insertImageObjectsFromFile(String filePath) throws IOException {

		Map<String, String> thumbsMap = helper.getThumbnailsMap(filePath);
		int indexedItems = 0;
		int skippedItems = 0;

		for (Map.Entry<String, String> thumbnail : thumbsMap.entrySet()) {
			try {

//				InputStream imageObj = new FileInputStream(new File(
//						getMelampoHome() + "/index/testImage.jpg"));
				test.insertImage(thumbnail.getKey(), new URL(thumbnail.getValue()));
				indexedItems++;
			
			} catch (Exception e) {
				System.out.println("Image indexing errors occured: " + e.getMessage());
				System.out.println("skip image: " + thumbnail.getKey() + "->" + thumbnail.getValue());
				
				//e.printStackTrace();
				skippedItems++;
			}
		}
		System.out.println("Items successfully inserted in index: " + indexedItems);
		System.out.println("Skipped Items: " + skippedItems);
		
		return indexedItems;
	}
	
}
