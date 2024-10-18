package utils;

import pages.TestExecutor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;

import org.apache.commons.lang.WordUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cucumber.api.Scenario;

public class WriteExcelReport extends TestExecutor {

	public XSSFWorkbook book;
	public XSSFSheet sheet;
	public FileOutputStream fileOut;
	public FileInputStream fileIn;
	public int existingRow = 0;
	public int runAttempt = 0;

	public void createOutputExcelFile() {
		try {
			fileOut = new FileOutputStream(filePath);
			book = new XSSFWorkbook();
			sheet = book.createSheet("test-results");
			sheet.createRow(0);
			setCell(0, 0, "S. No.");
			setCell(0, 1, "FeatureFile");
			setCell(0, 2, "ScenarioTagName");
			setCell(0, 3, "ScenarioName");
			setCell(0, 4, "RunAttempt");
			setCell(0, 5, "Status");
		} catch (IOException e) {
			System.out.println("Not able to create file in the path: " + filePath);
		}
	}

	public void appendOutputExcelFile(Scenario sce) {
		try {
			fileIn = new FileInputStream(filePath);
			book = new XSSFWorkbook(fileIn);
			sheet = book.getSheetAt(0);
			searchTextInExcelForRunAttemptCalculation(sce);
			fileIn.close();
			fileOut = new FileOutputStream(filePath);
		} catch (org.apache.poi.EmptyFileException e1) {
			System.out.println("Empty file detected while writing excel-output file!");
		} catch (IOException e2) {
			System.out.println("Not able to append file in the path: " + filePath);
			e2.printStackTrace();
		}
	}

	public void searchTextInExcelForRunAttemptCalculation(Scenario sce) {
		DataFormatter formatter = new DataFormatter();
		String tagToSearch = sce.getName().split(":")[0].trim();
		Cell tagCell, runAttCell;
		for (Row row : sheet) {
			tagCell = row.getCell(2);
			runAttCell = row.getCell(4);
			if (formatter.formatCellValue(tagCell).equals(tagToSearch)) {
				runAttempt = Integer.parseInt(formatter.formatCellValue(runAttCell));
				runAttempt++;
				existingRow = row.getRowNum();
				break;
			}
		}
	}

	public void createOrAppendExcelFile(Scenario sce) {
		File file = new File(filePath);
		if (!file.exists())
			createOutputExcelFile();
		else if (file.length() == 0) {
			file.delete();
			createOrAppendExcelFile(sce);
		} else
			appendOutputExcelFile(sce);
	}

	public void setCell(int row, int col, String value) {
		sheet.getRow(row).createCell(col).setCellValue(value);
	}

	public void updateScenarioResult(String scenarioId, int sNo, String tagName, String scenarioName, String status) {
		String featureNm = getFeatureName(scenarioId);
		System.out.println("SNo : " + sNo);
		System.out.println("FeatureName : " + featureNm);
		System.out.println("Scenario : " + scenarioName);
		System.out.println("Tag : " + tagName);
		System.out.println("Status : " + WordUtils.capitalize(status));
		sheet.createRow(sNo);
		setCell(sNo, 0, String.valueOf(sNo));
		setCell(sNo, 1, featureNm);
		setCell(sNo, 2, tagName);
		setCell(sNo, 3, scenarioName);
		setCell(sNo, 4, WordUtils.capitalize(status));
	}

	public void updateScenarioResult(String featureNm, String tagName, String scenarioName, String runAttemptCalculated,
			String status) {
		int lastRow = getLastRowNo() + 1;
		System.out.printf(
				"XLData ==> SNo : %s, FeatureName: %s, Scenario : %s, Tag : %s, RunAttempt : %s, Status : %s\n",
				lastRow, featureNm, scenarioName, tagName, runAttemptCalculated, status);
		int rowToUpdateData = (runAttempt > 0) ? existingRow : lastRow;
		sheet.createRow(rowToUpdateData);
		setCell(rowToUpdateData, 0, String.valueOf(rowToUpdateData));
		setCell(rowToUpdateData, 1, featureNm);
		setCell(rowToUpdateData, 2, tagName);
		setCell(rowToUpdateData, 3, scenarioName);
		setCell(rowToUpdateData, 4, runAttemptCalculated);
		setCell(rowToUpdateData, 5, status);
	}

	public void updateScenarioResult(Scenario sce) {
		String featureNm = getFeatureName(sce.getId()).split(":")[0].trim();
		String tag = sce.getName().split(":")[0].trim();
		String scenario = sce.getName().split(":")[1].trim();
		String statusUC = WordUtils.capitalize(sce.getStatus());
		updateScenarioResult(featureNm, tag, scenario, String.valueOf(runAttempt), statusUC);
		writeExcel();
		closeExcel();
	}

	public void writeExcel() {
		try {
			book.write(fileOut);
		} catch (IOException e) {
			System.out.println("Not able to write data into excel sheet");
		}
	}

	public void closeExcel() {
		try {
			book.close();
			fileOut.flush();
			fileOut.close();
		} catch (IOException e) {
			System.out.println("Exception occured in closing the excel sheet");
		}
	}

	public int getLastRowNo() {
		return sheet.getLastRowNum();
	}

	public String getFeatureName(String scenarioId) {
		String rawFeatureName = scenarioId.split(";")[0].replace("-", " ");
		return rawFeatureName.substring(0, 1).toUpperCase() + rawFeatureName.substring(1);
	}

}
