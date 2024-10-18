package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;

import pages.TestExecutor;

public class CSVFileReader extends TestExecutor {

	BufferedReader reader = null;
	Map<String, String> inmap;

	public String getCSVData(int row, String columnNeeded) throws IOException {
		inmap = new HashMap<String, String>();
		reader = new BufferedReader(new FileReader(csvInputPath));
		String line = null, splitStr1[], splitStr2[];
		int lineNo = 0;
		String firstLine = reader.readLine();
		while ((line = reader.readLine()) != null) {
			lineNo++;
			if (row == lineNo)
				break;
		}
		splitStr1 = firstLine.split(",");
		splitStr2 = line.split(",");
		for (int i = 0; i < splitStr2.length; i++) {
			inmap.put(splitStr1[i], splitStr2[i]);
		}
		reader.close();
		return inmap.get(columnNeeded);
	}

	public String getCSVData(String filePath, int row, String columnNeeded) throws IOException {
		inmap = new HashMap<String, String>();
		reader = new BufferedReader(new FileReader(filePath));
		String line = null, splitStr1[], splitStr2[];
		int lineNo = 0;
		String firstLine = reader.readLine();
		while ((line = reader.readLine()) != null) {
			lineNo++;
			if (row == lineNo)
				break;
		}
		splitStr1 = firstLine.split(",");
		splitStr2 = line.split(",");
		for (int i = 0; i < splitStr2.length; i++) {
			inmap.put(splitStr1[i], splitStr2[i]);
		}
		reader.close();
		return inmap.get(columnNeeded);
	}
	
	public String getCSVData(String filePath, int row, String columnNeeded , Character delimiter) throws IOException {
		inmap = new HashMap<String, String>();
		reader = new BufferedReader(new FileReader(filePath));
		String line = null, splitStr1[], splitStr2[];
		int lineNo = 0;
		String firstLine = reader.readLine();
		while ((line = reader.readLine()) != null) {
			lineNo++;
			if (row == lineNo)
				break;
		}
		if(delimiter.equals('|'))
		{
			splitStr1 = firstLine.split("\\|");
			System.out.println(splitStr1[1]);
			splitStr2 = line.split("\\|");
		}
		else
		{
		String spl = Character.toString(delimiter);
		splitStr1 = firstLine.split(spl);
		splitStr2 = line.split(spl);
		}
		for (int i = 0; i < splitStr2.length; i++) {
			inmap.put(splitStr1[i], splitStr2[i]);
		}
		reader.close();
		return inmap.get(columnNeeded);
	}
	
	public String getCSVDatasplittab(String filePath, int row, String columnNeeded) throws IOException {
		inmap = new HashMap<String, String>();
		reader = new BufferedReader(new FileReader(filePath));
		String line = null, splitStr1[], splitStr2[];
		int lineNo = 0;
		String firstLine = reader.readLine();
		while ((line = reader.readLine()) != null) {
			lineNo++;
			if (row == lineNo)
				break;
		}
		splitStr1 = firstLine.split("\\s+");
		splitStr2 = line.split("\\s+");
		for (int i = 0; i < splitStr2.length; i++) {
			inmap.put(splitStr1[i], splitStr2[i]);
		}
		reader.close();
		return inmap.get(columnNeeded);
	}
	
	public String getCSVDatawithnull(String filePath, int row, String columnNeeded) throws IOException {
		inmap = new HashMap<String, String>();
		reader = new BufferedReader(new FileReader(filePath));
		String line = null, splitStr1[], splitStr2[];
		int lineNo = 0;
		String firstLine = reader.readLine();
		while ((line = reader.readLine()) != null) {
			lineNo++;
			if (row == lineNo)
				break;
		}
		splitStr1 = firstLine.split(",");
		splitStr2 = line.split(",");
		for (int i = 0; i < splitStr2.length; i++) {
			inmap.put(splitStr1[i], splitStr2[i]);
		}
		reader.close();
		if(inmap.containsKey(columnNeeded))
		{
			return inmap.get(columnNeeded);
		}
		else
			return null;
	}
	
	public String getCSVDatajson(String filePath, int row, String columnNeeded) throws IOException {
		inmap = new HashMap<String, String>();
		inmap.remove(columnNeeded);
		String[] splitStr1 = new String[100], splitStr2 = new String[100];
		String lines = Files.readAllLines(Paths.get(filePath)).get(row-1);
		String str = lines.replaceAll("\"", "").replace("{", "").replace("}", "").replace("[", "").replace("]", "");
		splitStr1 = str.split(",");
		for (int i = 0; i < splitStr1.length; i++) {
			splitStr2 = splitStr1[i].split(":");
			inmap.put(splitStr2[0], splitStr2[1]);
		}
		return inmap.get(columnNeeded);
	}

	public int getRowNumber(String fileName, String column, String rowDataToFind) throws IOException {
		try (CSVReader csvReader = new CSVReader(new FileReader(fileName), ',')) {
			List<String[]> records = csvReader.readAll();
			int i, j;
			boolean foundRowData = false;
			for (i = 0; i < records.get(0).length; i++)
				if (records.get(0)[i].equals(column))
					break;
			for (j = 0; j < records.size(); j++)
				if (records.get(j)[i].equals(rowDataToFind)) {
					foundRowData = true;
					break;
				}
			csvReader.close();
			if (foundRowData)
				return j;
			else
				return -1;
		}
	}
	
	public int getRowNumber(String fileName, String column, String rowDataToFind, Character delimiter) throws IOException {
		try (CSVReader csvReader = new CSVReader(new FileReader(fileName), delimiter)) {
			List<String[]> records = csvReader.readAll();
			int i, j;
			boolean foundRowData = false;
			for (i = 0; i < records.get(0).length; i++)
				if (records.get(0)[i].equals(column))
					break;
			for (j = 0; j < records.size(); j++)
				if (records.get(j)[i].equals(rowDataToFind)) {
					foundRowData = true;
					break;
				}
			csvReader.close();
			if (foundRowData)
				return j;
			else
				return -1;
		}
	}
	
	public int getColumnNumber(String fileName, String column, String rowDataToFind) throws IOException {
		try (CSVReader csvReader = new CSVReader(new FileReader(fileName), ',')) {
			List<String[]> records = csvReader.readAll();
			int i, j;
			boolean foundRowData = false;
			for (i = 0; i < records.get(0).length; i++)
				if (records.get(0)[i].equals(column))
					break;
			for (j = 0; j < records.size(); j++)
				if (records.get(j)[i].equals(rowDataToFind)) {
					foundRowData = true;
					break;
				}
			csvReader.close();
			if (foundRowData)
				return j;
			else
				return -1;
		}
	}
}
