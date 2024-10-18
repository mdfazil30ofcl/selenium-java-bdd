package tests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GetAllFeatureNames {

	public static void main(String[] args) {
		List<String> featureFiles = getFeatureFiles("src/test/java/features");

		System.out.println("List of Feature Files:");
		for (String featureFile : featureFiles) {
			System.out.println(featureFile);
		}
	}

	private static List<String> getFeatureFiles(String directoryPath) {
		List<String> featureFiles = new ArrayList<>();

		File directory = new File(directoryPath);
		if (directory.exists() && directory.isDirectory()) {
			findFeatureFiles(directory, featureFiles);
		} else {
			System.err.println("Invalid directory path: " + directoryPath);
		}

		return featureFiles;
	}

	private static void findFeatureFiles(File directory, List<String> featureFiles) {
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					findFeatureFiles(file, featureFiles);
				} else if (file.isFile() && file.getName().endsWith(".feature")) {
					featureFiles.add(file.getPath());
				}
			}
		}
	}
}
