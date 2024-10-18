package steps;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;

import com.vimalselvam.cucumber.listener.ExtentCucumberFormatter;
import com.vimalselvam.cucumber.listener.Reporter;

import atu.testrecorder.ATUTestRecorder;
import atu.testrecorder.exceptions.ATUTestRecorderException;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import pages.TestExecutor;

public class StartingSteps extends TestExecutor {

	public static String videoFileName = null;
	public static String browserLogFileName = null;
	public static String htmlFileName = null;
	public static String scenarioName = "";
	public static String status = "";
	public static String tags[] = { "" };
	public static String featureName = "";
	public static String reason = "";
	public static String comments = "";

	@Before
	public void beforeScenario(Scenario sce) throws ClassNotFoundException, IOException, ATUTestRecorderException {
		initializeClasses();
		writeXL.createOrAppendExcelFile(sce);
		Reporter.loadXMLConfig(userDir + "/src/main/java/config/extent-config.xml");
		String currentTime = common.getCurrentDate("dd.MM.yyyy HH-mm-ss");
		videoFileName = "testVideo_" + currentTime;
		browserLogFileName = "logFile_" + currentTime + ".txt";
		recorder = new ATUTestRecorder(videoPath, videoFileName, false);
		recorder.start();
		featureName = sce.getId();
		scenarioName = sce.getName();
		tags = sce.getSourceTagNames().toArray(new String[0]);
		if (!dunit) {
			htmlFileName = "AutomationHTMLReport" + currentTime + ".html";
			Runtime.getRuntime().addShutdownHook(common.new TriggerEndEmail());
			if (propertyReader.readPropertyEmail("TriggerEmail_StartOfRun").equals("Yes")) {
				sendEmail.emailSubject = "Script Execution Started";
				sendEmail.sendEmail();
			}
			if (propertyReader.readPropertyEmail("TriggerEmail_EndOfRun").equals("Yes"))
				sendEmail.emailSubject = propertyReader.readPropertyEmail("EmailSubject");
			emailGen.generateEmailBody();
			dunit = true;
		}
	}

	@After
	public void afterScenario(Scenario sce) throws IOException, ATUTestRecorderException, InterruptedException {
		status = sce.getStatus().toUpperCase();
		if (sce.isFailed()) {
			reason = common.messageToPrint;
		}
		emailGen.writeScenarioRowToHTML();
		String videoFilePath = (propertyReader.isRunUnderJenkins) ? "../../ScriptVideos/" : "file:///" + videoPath;
		Reporter.addStepLog("<a href='" + videoFilePath + videoFileName + ".mov'>Click for Video</a> <span> [Use VLC Media Player]</span>");
		
		try {
			if (sce.isFailed()) 
				common.writeScreenshot();			
		} finally {
			String browserLogFilePath = (propertyReader.isRunUnderJenkins) ? "../../browserlogs/"
					: "file:///" + browserLogPath;
			BufferedWriter bw = new BufferedWriter(new FileWriter(browserLogPath + browserLogFileName, true));
			LogEntries logs = driver.manage().logs().get("browser");
			for (LogEntry entry : logs) {
				browserLogMessage = entry.getMessage();
				bw.write((new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + browserLogMessage) + "\r\n");
			}
			bw.flush();
			try {
				if (bw != null) {
					bw.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			Reporter.addStepLog("<a href='" + browserLogFilePath + browserLogFileName
					+ "' target=\"_blank\">Click for Browser Logs</a>");
			ExtentCucumberFormatter e = new ExtentCucumberFormatter(new java.io.File(extentReportFilePath));
			e.done();
			e.close();
			recorder.stop();
			writeXL.updateScenarioResult(sce);
			if (propertyReader.readProperty("CloseBrowser").equals("Yes"))
				driverFactory.destroyDriver();
			else if (!sce.isFailed()) {
				driverFactory.destroyDriver();
			}
		}
		System.out.println("Inside at After for Scenario: " + sce.getName());
	}
}