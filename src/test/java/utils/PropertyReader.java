package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import pages.TestExecutor;

public class PropertyReader extends TestExecutor {
	Properties properties = new Properties();
	Properties propertiesEmail = new Properties();
	Properties loadcontactProperties = new Properties();
	InputStream inputStream = null;
	InputStream inputStreamEmail = null;

	public String configFile;
	public boolean isRunUnderJenkins = false;

	public PropertyReader() {
		loadProperties();
		loadEmailProperties();
	}

	public String getPropertyConfigFile() {
		isRunUnderJenkins = System.getenv("JENKINS_HOME") == null ? false : true;
		return isRunUnderJenkins ? System.getenv("CONFIG_FILE_NAME") : "Config.properties";
	}

	private void loadProperties() {
		try {
			inputStream = new FileInputStream("src/main/java/config/" + getPropertyConfigFile());
			properties.load(inputStream);
			configFile = getPropertyConfigFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadEmailProperties() {
		try {
			if (isRunUnderJenkins)
				inputStreamEmail = new FileInputStream("src/main/java/config/" + getPropertyConfigFile());
			else
				inputStreamEmail = new FileInputStream("src/main/java/config/email-config.properties");
			propertiesEmail.load(inputStreamEmail);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String readProperty(String key) {
		return properties.getProperty(key);
	}

	public String readPropertyEmail(String key) {
		return propertiesEmail.getProperty(key);
	}

	public void putProperty(String key, String value) {
		properties.put(key, value);
	}
}
