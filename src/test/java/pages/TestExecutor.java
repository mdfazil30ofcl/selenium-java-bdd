package pages;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.openqa.selenium.WebDriver;
import atu.testrecorder.ATUTestRecorder;
import atu.testrecorder.exceptions.ATUTestRecorderException;
import utils.CSVFileReader;
import utils.ChangeServerDateTimeUtils;
import utils.Common;
import utils.CopyFileUtils;
import utils.DataUtils;
import utils.DownloadFileUtils;
import utils.DriverFactory;
import utils.EmailGeneratorUtils;
import utils.PropertyReader;
import utils.SendEmailUtils;
import utils.WriteExcelReport;

public class TestExecutor {

	public static WebDriver driver = null;
	public static final String userDir = System.getProperty("user.dir");
	public static final String hubURL = "http://localhost:4444/wd/hub";
	public static String windowID = null;
	
	public static LoginPage loginPage;	
	
	public static ChangeServerDateTimeUtils csdt;
	public static SendEmailUtils sendEmail;
	public static EmailGeneratorUtils emailGen;
	public static WriteExcelReport writeXL;
	public static Common common;
	public static PropertyReader propertyReader;
	public static CSVFileReader csv;
	public static DriverFactory driverFactory;
	public static DataUtils dataUtils;
	public static CopyFileUtils copyFiles;
	public static DownloadFileUtils downloads;
	public static ATUTestRecorder recorder;

	public static Map<String, String> map = new HashMap<String, String>();
	public DataFormatter formatter = new DataFormatter();
	public static int timeOut = 15;
	public static String downloadsPath = userDir + "\\src\\main\\java\\downloads\\";
	public static String imageFilePath = userDir + "\\src\\main\\java\\img\\";
	public static String extentReportFilePath = userDir + "/test-output/extentreports/LCM4.0/extent.html";
	public static String videoPath = userDir + "/test-output/ScriptVideos/";
	public static String browserLogPath = userDir + "/test-output/browserlogs/";
	public static String htmlFilesPath = userDir + "/test-output/htmlfiles/";
	public static String sqlFilesPath = userDir + "\\src\\main\\java\\sqlfiles\\";
	public static String cucumberGeneratedRerunFilePath = userDir + "/target/rerun.txt";
	public static String copyRerunToLocalFolderPath = userDir + "/test-output/rerunFiles/";
	public static String inputFilePath = userDir + "/src/main/java/input/";
	public static String tempFilePath = inputFilePath + "Temp.properties";
	public static int addSleepTime = 0;
	public static String filePath = userDir + "/test-output/excel_report.xlsx";
	public static String csvInputPath = null;
	public static String credentials;
	public static String browserLogMessage = null;
	public static boolean dunit = false;
	public static int scenarioCounter = 0;

	public void initializeClasses() throws IOException, ATUTestRecorderException {
		propertyReader = new PropertyReader();
		driverFactory = new DriverFactory();
		loginPage = new LoginPage();
		
		dataUtils = new DataUtils();
		copyFiles = new CopyFileUtils();
		downloads = new DownloadFileUtils();
		common = new Common();
		csdt = new ChangeServerDateTimeUtils();
		sendEmail = new SendEmailUtils();
		emailGen = new EmailGeneratorUtils();
		csv = new CSVFileReader();
		writeXL = new WriteExcelReport();
		
	}

	public String getData(String columnName) {
		return dataUtils.getMapValue(columnName);
	}

	public void writeReport(String status, String message, boolean screenShotNeeded) {
		System.out.println(common.getCurrentDate("hh:mm:ss:SSS") + " - " + message);
		common.writeReport(status, message, screenShotNeeded);
	}
}
