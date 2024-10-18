package utils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

import pages.TestExecutor;

public class DownloadFileUtils extends TestExecutor {

	public boolean isFileDownloaded(String fileName) {
		boolean flag = false;
		File dir = new File(downloadsPath);
		File[] dir_contents = dir.listFiles();
		if (dir_contents == null || dir_contents.length == 0) {
			flag = false;
		}
		for (int i = 0; i < dir_contents.length; i++) {
			if (dir_contents[i].getName().equals(fileName))
				return flag = true;
		}
		return flag;
	}

	public String waitAndgetDownloadedDocumentName(String fileExtension) {
		String downloadedFileName = null;
		boolean valid = true;
		boolean found = false;

		// default timeout in seconds
		long timeOut = 20;
		try {
			System.out.println("Downloaded files path: " + downloadsPath);
			Path downloadFolderPath = Paths.get(downloadsPath);
			WatchService watchService = FileSystems.getDefault().newWatchService();
			downloadFolderPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
			long startTime = System.currentTimeMillis();
			do {
				WatchKey watchKey;
				watchKey = watchService.poll(timeOut, TimeUnit.SECONDS);
				long currentTime = (System.currentTimeMillis() - startTime) / 1000;
				if (currentTime > timeOut) {
					System.out.println("Download operation timed out.. Expected file was not downloaded");
					return downloadedFileName;
				}
				for (WatchEvent<?> event : watchKey.pollEvents()) {
					Kind<?> kind = event.kind();
					if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
						String fileName = event.context().toString();
						System.out.println("New File Created:" + fileName);
						if (fileName.endsWith(fileExtension)) {
							downloadedFileName = fileName;
							System.out.println("Downloaded file found with extension " + fileExtension
									+ ". File name is " + fileName);
							Thread.sleep(500);
							found = true;
							break;
						}
					}
				}
				if (found) {
					return downloadedFileName;
				} else {
					currentTime = (System.currentTimeMillis() - startTime) / 1000;
					if (currentTime > timeOut) {
						System.out.println("Failed to download expected file");
						return downloadedFileName;
					}
					valid = watchKey.reset();
				}
			} while (valid);
		}

		catch (InterruptedException e) {
			System.out.println("Interrupted error - " + e.getMessage());
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("Download operation timed out.. Expected file was not downloaded");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Error occured - " + e.getMessage());
			e.printStackTrace();
		}
		return downloadedFileName;
	}

	public static void test() {
		DataFormatter formatter = new DataFormatter();
		try {
			FileInputStream file = new FileInputStream(new File("C:\\testxl.xlsx"));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			try {
				int rowCount = sheet.getLastRowNum() + 1;
				int colCount = sheet.getRow(0).getLastCellNum();
				Row currRow;
				for (int i = 0; i < rowCount; i++) {
					System.out.println();
					for (int j = 0; j < colCount; j++) {
						currRow = sheet.getRow(i);
						System.out.print(formatter.formatCellValue(currRow.getCell(j))+ ",");
					}
				}
			} catch (NullPointerException e) {
				System.out.println("Row number entered exceeds the actual row count of excel data sheet");
			}
			workbook.close();
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		test();
	}

}
