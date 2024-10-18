package utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import pages.TestExecutor;

@SuppressWarnings("unused")
public class DriverFactory extends TestExecutor {

	public DriverFactory() throws MalformedURLException {
		initialize();
	}

	public void initialize() throws MalformedURLException {
		if (driver != null)
			destroyDriver();
		createNewDriverInstance();
	}

	private void createNewDriverInstance() {
		String browser = new PropertyReader().readProperty("browser");
		String runMode = new PropertyReader().readProperty("runMode");
		browser = runMode + browser;
		System.out.println("Run in : " + browser);
		switch (browser) {
		case "LocalChrome":
			if (propertyReader.isRunUnderJenkins)
				killProcess("chrome");
			System.setProperty("webdriver.chrome.driver", userDir + "/resources/chromedriver.exe");
			LoggingPreferences logPrefs = new LoggingPreferences();
			logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--allow-running-insecure-content");
			options.addArguments("-ignore-certificate-errors");
			options.addArguments("use-fake-ui-for-media-stream"); // allow microphone access by chrome
			options.addArguments("disable-infobars"); // disabling infobars
			options.addArguments("--disable-notifications");
			options.addArguments("--disable-extensions"); // disabling extensions
			options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
			options.setExperimentalOption("excludeSwitches", new String[] { "load-extension", "enable-automation" });
			// -----------Code Snippet for downloading files through code-----------
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.prompt_for_download", "false");
			chromePrefs.put("download.default_directory", downloadsPath);
			// -----------Code Snippet for disabling password saving popup-----------
			chromePrefs.put("credentials_enable_service", false);
			chromePrefs.put("profile.password_manager_enabled", false);
			options.setExperimentalOption("prefs", chromePrefs);
			// -----------Code Snippet for getting XSRF-TOKEN from browser-----------
			options.setCapability("goog:loggingPrefs", logPrefs);
			// ---------------------------------------------------------------------
			driver = new ChromeDriver(options);
			driver.manage().deleteAllCookies();
			break;

		case "LocalChrome_SameProfile":
			System.setProperty("webdriver.chrome.driver", userDir + "/resources/chromedriver.exe");
			LoggingPreferences logPrefs1 = new LoggingPreferences();
			logPrefs1.enable(LogType.PERFORMANCE, Level.ALL);
			ChromeOptions options1 = new ChromeOptions();
			options1.addArguments("user-data-dir=" + System.getProperty("user.home")
					+ "/AppData/Local/Google/Chrome/User Data/Automation");
			System.out.println(System.getProperty("user.home"));
			options1.addArguments("profile-directory=Profile 1");
			options1.addArguments("--allow-running-insecure-content");
			options1.addArguments("-ignore-certificate-errors");
			options1.addArguments("use-fake-ui-for-media-stream"); // allow microphone access by chrome

			options1.addArguments("disable-infobars"); // disabling infobars
			options1.addArguments("--disable-notifications");
			options1.addArguments("--disable-extensions"); // disabling extensions
			options1.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
			options1.addArguments("--no-sandbox"); // Bypass OS security model
			options1.setExperimentalOption("excludeSwitches", new String[] { "load-extension", "enable-automation" });
			// -----------Code Snippet for downloading files through code-----------
			HashMap<String, Object> chromePrefs1 = new HashMap<String, Object>();
			chromePrefs1.put("profile.default_content_settings.popups", 0);
			chromePrefs1.put("download.prompt_for_download", "false");
			chromePrefs1.put("download.default_directory", downloadsPath);
			// -----------Code Snippet for disabling password saving popup-----------
			chromePrefs1.put("credentials_enable_service", false);
			chromePrefs1.put("profile.password_manager_enabled", false);
			options1.setExperimentalOption("prefs", chromePrefs1);
			// -----------Code Snippet for getting XSRF-TOKEN from browser-----------
			options1.setCapability("goog:loggingPrefs", logPrefs1);
			// ---------------------------------------------------------------------
			driver = new ChromeDriver(options1);
			driver.manage().deleteAllCookies();
			break;
		}
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void killProcess(String str) {
		try {
			String killProcess = "taskkill /F /IM " + str + ".exe";
			System.out.println("Killing process with command: " + killProcess);
			Runtime.getRuntime().exec(killProcess);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void destroyDriver() {
		driver.quit();
		driver = null;
	}
}