package tests;

import com.github.mkolisnyk.cucumber.reporting.CucumberResultsOverview;

import java.io.File;
import java.util.*;

public class CucumberReportsTest {

	public static String src = "C:\\json";

	public static void main(String args[]) {
		generateCucumberOverviewReport();
	}

	public static void generateCucumberOverviewReport() {
		CucumberResultsOverview results = new CucumberResultsOverview();
		File srcDir = new File(src);
		List<String> jsonFiles = new ArrayList<>();
		for (File f : srcDir.listFiles())
			if (f.getName().contains("json"))
				jsonFiles.add(f.getAbsolutePath());
		Object[] arr = jsonFiles.toArray();
		String str[] = new String[arr.length];
		System.arraycopy(arr, 0, str, 0, arr.length);
		results.setSourceFiles(str);
		results.setOutputDirectory(src);
		results.setOutputName("OverallReports");
		try {
			results.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}