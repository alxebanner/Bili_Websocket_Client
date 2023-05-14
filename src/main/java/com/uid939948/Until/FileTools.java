package com.uid939948.Until;

import org.springframework.boot.system.ApplicationHome;

import java.io.File;


public class FileTools {
	public File getBaseJarPath() {
		ApplicationHome home = new ApplicationHome(getClass());
		File jarFile = home.getSource();
		return jarFile.getParentFile();
	}
}
