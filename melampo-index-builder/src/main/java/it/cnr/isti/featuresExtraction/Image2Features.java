package it.cnr.isti.featuresExtraction;

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
	private LireFeature[] lireExtractors;

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

	public String image2Features(InputStream imgStream)
			throws FeaturesExtractionException {
		BufferedImage img;
		try {
			img = ImageIO.read(imgStream);
		} catch (IOException e) {
			throw new FeaturesExtractionException(e);
		}
		String res = extractFeatures(img);
		return res;
	}

	public String image2Features(File imgFile)
			throws FeaturesExtractionException {
		BufferedImage img;
		try {
			img = ImageIO.read(new FileInputStream(imgFile));
		} catch (IOException e) {
			throw new FeaturesExtractionException(e);
		}
		String res = extractFeatures(img);
		return res;
	}

	public String image2Features(URL imgURL)
			throws FeaturesExtractionException {
		BufferedImage img;
		try {
			img = ImageIO.read(imgURL);
		} catch (IOException e) {
			throw new FeaturesExtractionException(e);
		}
		String res = extractFeatures(img);
		return res;
	}

	private String extractFeatures(BufferedImage buffImg)
			throws FeaturesExtractionException {
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
				new FeaturesExtractionException("error, unable to extract " + extractorName);
			}
		}
		features.append("</lire>\n</IRImage>");
		//System.out.println("features:" + features.toString());
		return features.toString();
	}
}