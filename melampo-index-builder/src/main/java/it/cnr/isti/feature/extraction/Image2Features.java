package it.cnr.isti.feature.extraction;

import it.cnr.isti.melampo.tools.Tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import net.semanticmetadata.lire.imageanalysis.LireFeature;

public class Image2Features {

	private String[][] extractorsImpl;
	LireFeature[] lireExtractors;
	
	protected Logger getLogger() {
		return Logger.getLogger(getClass());
	}

	public Image2Features(File confDir) throws IOException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		extractorsImpl = Tools.getAllOrderedProperties(new File(confDir,
				"image-fx.properties"));

		lireExtractors = new LireFeature[extractorsImpl.length];
		for (int i = 0; i < extractorsImpl.length; i++) {
			String extractorImpl = extractorsImpl[i][1];

			lireExtractors[i] = (LireFeature) Class.forName(extractorImpl)
					.newInstance();
		}
	}

	public String extractFeatures(InputStream imgStream)
			throws FeatureExtractionException {
		
		String res = null;
		try {
			BufferedImage img = ImageIO.read(imgStream);
			res = extractFeatures(img);
		} catch (IOException e) {
			throw new FeatureExtractionException(e);
		}finally{
				closeStream(imgStream);
		}
		return res;
	}

	public String extractFeatures(File imgFile)
			throws FeatureExtractionException {
		
		try {
			return extractFeatures(new FileInputStream(imgFile));
		} catch (FileNotFoundException e) {
			throw new FeatureExtractionException(e);
		}
	}

	public String extractFeatures(URL imgURL)
			throws FeatureExtractionException {
		
		try {
			return extractFeatures(ImageIO.read(imgURL));
		} catch (IOException e) {
			throw new FeatureExtractionException(e);
		}		
	}

	String extractFeatures(BufferedImage buffImg)
			throws FeatureExtractionException {
		StringBuilder features = new StringBuilder();
		features.append("<IRImage>\n<lire>\n");
		for (int i = 0; i < extractorsImpl.length; i++) {
			String extractorName = extractorsImpl[i][0];
			try {
				lireExtractors[i] = lireExtractors[i].getClass().newInstance();
				lireExtractors[i].extract(buffImg);
				features.append("<").append(extractorName).append(">");
				features.append(lireExtractors[i].getStringRepresentation());
				features.append("</").append(extractorName).append(">\n");
			} catch (Exception e) {
				new FeatureExtractionException("error, unable to extract " + extractorName);
			}
		}
		features.append("</lire>\n</IRImage>");
		//System.out.println("features:" + features.toString());
		return features.toString();
	}
	
	protected void closeStream(InputStream imgStream) {
		try {
			if(imgStream != null)
				imgStream.close();
		} catch (IOException e) {
			//this exception shouldn't occur. if it occures it is not harmfull
			getLogger().trace("Error when closing imgStream");
			getLogger().debug("Exception: ", e);
		}
	}
}