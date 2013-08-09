package it.cnr.isti.indexer;

import java.io.File;
import java.io.IOException;

public class BaseIndexingTest {

	File melampoHome = null;
	File confDir = null;

	public BaseIndexingTest() {
		super();
	}

	public File getMelampoHome() throws IOException {
		if (melampoHome == null)
			melampoHome = (new File("./src/test/resources/melampohome")).getCanonicalFile();
		return melampoHome;
	}

	protected File getConfDir() throws IOException {
		if (confDir == null)
			confDir = (new File(getMelampoHome() + "/conf")).getCanonicalFile();
		
		return confDir;
	}

}