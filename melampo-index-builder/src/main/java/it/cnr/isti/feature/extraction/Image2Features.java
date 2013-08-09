package it.cnr.isti.feature.extraction;

import it.cnr.isti.melampo.tools.Tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.imageanalysis.LireFeature;

public class Image2Features {

	private String[][] extractorsImpl;
	LireFeature[] lireExtractors;

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
		BufferedImage img;
		try {
			img = ImageIO.read(imgStream);
		} catch (IOException e) {
			throw new FeatureExtractionException(e);
		}
		String res = extractFeatures(img);
		return res;
	}

	public String extractFeatures(File imgFile)
			throws FeatureExtractionException {
		BufferedImage img;
		try {
			img = ImageIO.read(new FileInputStream(imgFile));
		} catch (IOException e) {
			throw new FeatureExtractionException(e);
		}
		String res = extractFeatures(img);
		return res;
	}

	public String extractFeatures(URL imgURL)
			throws FeatureExtractionException {
		BufferedImage img;
		try {
			img = ImageIO.read(imgURL);
		} catch (IOException e) {
			throw new FeatureExtractionException(e);
		}
		String res = extractFeatures(img);
		return res;
	}

	private String extractFeatures(BufferedImage buffImg)
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
}