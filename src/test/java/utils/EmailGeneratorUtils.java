package utils;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import pages.TestExecutor;
import steps.StartingSteps;

public class EmailGeneratorUtils extends TestExecutor {
	public String inputHTMLFilePath = userDir + "\\src\\main\\java\\input\\sample.html";
	public String inputHTMLFilePath1 = userDir + "\\src\\main\\java\\input\\sample1.html";
	public String titleOfReport = "AutomationTestResults";
	public String pageTitleOfReport = "AutomationTestResultsPage";
	public String testURLToInvoke = "URLTested";
	public String machineIPAddress = "MachineIPAddress";
	public String testerName = "NameOfTester";
	public String currentTime = "CurrentTime";

	public String row = "<tr style=\"background-color: #fad3cc;\" name=\"row-1\">\r\n"
			+ "<td style=\"\\&quot;text-align:\" center;\\\"=\"\" width=\"\\&quot;25%\\&quot;\"><font face=\"arial\" color=\"#262e36\" size=\"2\"><b>FeatureData</b></font></td>\r\n"
			+ "<td style=\"\\&quot;text-align:\" center;\\\"=\"\" width=\"\\&quot;20%\\&quot;\"><font face=\"arial\" color=\"#262e36\" size=\"2\"><b>TagData</b></font></td>\r\n"
			+ "<td style=\"\\&quot;text-align:\" center;\\\"=\"\" width=\"\\&quot;10%\\&quot;\"><font face=\"arial\" color=\"#262e36\" size=\"2\"><b>ScenarioData</b></font></td>\r\n"
			+ "<td style=\"\\&quot;text-align:\" center;\\\"=\"\" width=\"\\&quot;10%\\&quot;\"><font face=\"arial\" color=\"#262e36\" size=\"2\"><b>StatusData</b></font></td>\r\n"
			+ "<td style=\"\\&quot;text-align:\" center;\\\"=\"\" width=\"\\&quot;10%\\&quot;\"><font face=\"arial\" color=\"#262e36\" size=\"2\">ReasonData</font></td>\r\n"
			+ "<td style=\"\\&quot;text-align:\" center;\\\"=\"\" width=\"\\&quot;35%\\&quot;\"><font face=\"arial\" color=\"#262e36\" size=\"2\">CommentsData</font></td>\r\n"
			+ "</tr>\r\n" + "<!--AddMoreRows-->";

	public String getSampleHTMLTemplate() {
		String toReturn = null;
		try {
			if (sendEmail.emailSubject.contains("Start"))
				toReturn = IOUtils.toString(new FileReader(inputHTMLFilePath1));
			else
				toReturn = IOUtils.toString(new FileReader(inputHTMLFilePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return toReturn;
	}

	public String getEmailHTMLReport() {
		String htmlFilePath = htmlFilesPath + StartingSteps.htmlFileName;
		String toReturn = null;
		try {
			toReturn = IOUtils.toString(new FileReader(htmlFilePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return toReturn;
	}

	public String generateEmailBody() {
		String reportTitle = propertyReader.readPropertyEmail("ReportTitle");
		String pageTitle = propertyReader.readPropertyEmail("PageTitle");
		String url = propertyReader.readProperty("URL");
		String ipAddress = getLocalHostIP();
		String userName = System.getProperty("user.name");
		String currDateTime = common.getCurrentDate("EEEE, dd MMMM yyyy HH:mm:ss.SSS");
		String htmlTemplate = getSampleHTMLTemplate().replace(titleOfReport, reportTitle)
				.replace(pageTitleOfReport, pageTitle).replace(machineIPAddress, ipAddress)
				.replace(testURLToInvoke, url).replace(testerName, userName).replace(currentTime, currDateTime);
		if (!sendEmail.emailSubject.contains("Start"))
			writeHTML(htmlTemplate);
		return htmlTemplate;
	}

	public void writeHTML(String htmlContent) {
		try {
			String htmlFilePath = htmlFilesPath + StartingSteps.htmlFileName;
			BufferedWriter bw = new BufferedWriter(new FileWriter(htmlFilePath, false));
			bw.write(htmlContent);
			bw.flush();
			if (bw != null) {
				bw.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public String getLocalHostIP() {
		InetAddress localhost;
		String ipToReturn = null;
		try {
			localhost = InetAddress.getLocalHost();
			ipToReturn = (localhost.getHostAddress()).trim();
			System.out.println("System IP Address : " + ipToReturn);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ipToReturn;
	}

	public static void main(String args[]) {
		System.out.println("Email output: " + System.getProperty("user.name"));
	}

	public void writeScenarioRowToHTML() {
		String tag = "";
		for (String str : StartingSteps.tags) {
			tag = tag + str + ", ";
		}
		tag = common.subStringLastNoOfChr(tag, 2);
		String parsedFeatureNm = StringUtils.capitalize(StartingSteps.featureName.split(";")[0].replace("-", " "));
		String htmlContent = getEmailHTMLReport().replace("<!--AddMoreRows-->", row)
				.replace("FeatureData", parsedFeatureNm).replace("TagData", tag.replace(",", ", "))
				.replace("ScenarioData", StartingSteps.scenarioName).replace("StatusData", StartingSteps.status)
				.replace("ReasonData", StartingSteps.reason).replace("CommentsData", StartingSteps.comments);
		StartingSteps.reason = "";
		writeHTML(htmlContent);
	}
}