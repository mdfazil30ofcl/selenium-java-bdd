package runner;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import com.github.mkolisnyk.cucumber.runner.ExtendedCucumber;
import com.github.mkolisnyk.cucumber.runner.ExtendedCucumberOptions;
import com.vimalselvam.cucumber.listener.ExtentProperties;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.BeforeClass;
//******************************************To run with JUnit********************************************//
import org.junit.runner.RunWith;
import pages.TestExecutor;

@RunWith(ExtendedCucumber.class)
@ExtendedCucumberOptions(jsonReport = "target/cucumber1.json",
					        detailedReport = false,
					        detailedAggregatedReport = false,
					        overviewReport = false,
					        overviewChartsReport = false,
					        pdfPageSize = "A4 Landscape",
					        toPDF = true,
					        outputFolder = "target/extendedreports",
					        retryCount = 0)
@CucumberOptions(features = ".", 
							monochrome = true, 
							snippets = SnippetType.CAMELCASE, 
							glue = "steps",
							tags = {"@DemoSiteValidation"},
							plugin = { "pretty",
										"json:target/cucumber1.json",
										"rerun:test-output/rerunFiles/1/rerun.txt",
										"com.vimalselvam.cucumber.listener.ExtentCucumberFormatter:"
										}, 
							dryRun = false)
public class RunnerClassTest extends TestExecutor {	
	static String currentDate = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss").format(new Date());
	static String reportName = "extent_" + currentDate;
	
	@BeforeClass
    public static void setup() {		
        ExtentProperties extentProperties = ExtentProperties.INSTANCE;
        extentProperties.setReportPath("test-output/extentreports/Run_1/" + reportName + ".html");
    }
}