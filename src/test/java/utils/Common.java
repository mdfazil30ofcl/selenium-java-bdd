package utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.vimalselvam.cucumber.listener.Reporter;
import com.google.common.base.Function;
import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.msfscc.FileAttributes;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2CreateOptions;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import cucumber.api.DataTable;
import cucumber.api.Scenario;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import pages.TestExecutor;

public class Common extends TestExecutor {

	DateFormat dFormat = new SimpleDateFormat("yyyy.MM.dd_HH-mm-ss");
	public String messageToPrint;
	public static Set<String> featuresList = new TreeSet<String>();
	public static LocalDate todayDate = null;

	public Common() {
		PageFactory.initElements(driver, this);
	}


	// Sleep method
	public void sleep(int timeInMillisecs) {
		try {
			Thread.sleep(timeInMillisecs + addSleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Check if alert exists
	public boolean ifAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException Ex) {
			return false;
		}
	}

	// If Alert exist
	public boolean isAlertExists(int timeToWait) {
		int cnt = 0;
		while (cnt != timeToWait) {
			try {
				changeTimeOut(0);
				driver.switchTo().alert();
				// changeTimeOut(timeOut);
				return true;
			} catch (NoAlertPresentException Ex) {

				System.out.println("Waiting for the  (Iteration count) : " + cnt);

				common.sleep(1000);
				cnt++;
			}
		}
		return false;
	}

	// Check element present or not
	public boolean isExists(By by) {
		changeTimeOut(0);
		try {
			return driver.findElement(by).isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		} finally {
			changeTimeOut(timeOut);
		}
	}

	public boolean isNotExists(String xpath, String msg) {
		By by = By.xpath(xpath);
		changeTimeOut(0);
		try {
			return !driver.findElement(by).isDisplayed();
		} catch (NoSuchElementException e) {
			writeReport("Fail", msg + " is displayed", true);
			return false;
		} finally {
			changeTimeOut(timeOut);
		}
	}

	public boolean isExists(WebElement element) {
		try {
			return element.isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public boolean isEnabled(WebElement element) {
		try {
			return element.isEnabled();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public boolean isEnabled(By by) {
		try {
			return driver.findElement(by).isEnabled();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public void isEnabled(WebElement element, String msg) {
		if (element.isEnabled())
			writeReport("Pass", msg + " is Enabled", false);
		else
			writeReport("Fail", msg + " is disabled", true);
	}

	public void isNotEnabled(WebElement element, String msg) {
		if (!element.isEnabled())
			writeReport("Pass", msg + " is disabled", false);
		else
			writeReport("Fail", msg + " is enabled", true);
	}

	public void isNotDisplayed(WebElement element, String msg) {
		if (!element.isDisplayed())
			writeReport("Pass", msg + " is not displayed", false);
		else
			writeReport("Fail", msg + " is displayed", true);
	}

	public void isNotEnabled(String xpath, String msg) {
		if (!driver.findElement(By.xpath(xpath)).isEnabled())
			writeReport("Pass", msg + " is disabled", false);
		else
			writeReport("Fail", msg + " is enabled", true);
	}

	public boolean isExists(WebElement element, int timeToWait) {
		int cnt = 0;
		while (cnt != timeToWait) {
			try {
				changeTimeOut(0);
				element.isDisplayed();
				changeTimeOut(timeOut);
				return true;
			} catch (NoSuchElementException e) {

				System.out.println("Waiting for the element (Iteration count) : " + cnt);

				common.sleep(500);
				cnt++;
			}
		}
		return false;
	}

	public boolean isExistsForEverySecond(WebElement element, int timeToWait) {
		int cnt = 0;
		while (cnt != timeToWait) {
			try {
				changeTimeOutInMilliSeconds(500);
				element.isDisplayed();
				// changeTimeOut(cnt);
				return true;
			} catch (NoSuchElementException e) {

				System.out.println("Waiting for the element (Iteration count) : " + cnt);

				common.sleep(500);
				cnt++;
			}
		}
		return false;
	}

	public boolean valueIsExistsForEverySecond(By by, String toCheck, int timeToWait) {
		int cnt = 0;
		while (cnt != timeToWait) {
			try {
				changeTimeOut(0);
				System.out.println("Callstatusmsg : " + driver.findElement(by).getText());
				if (driver.findElement(by).getText().equals(toCheck))
					// changeTimeOut(cnt);
					return true;
			} catch (NoSuchElementException e) {

				System.out.println("Waiting for the element (Iteration count) : " + cnt);

				common.sleep(500);
				cnt++;
			}
		}
		return false;
	}

	public boolean isExists(By by, int timeToWait) {
		int cnt = 0;
		while (cnt != timeToWait) {
			try {
				changeTimeOut(0);
				driver.findElement(by).isDisplayed();
				changeTimeOut(timeOut);
				return true;
			} catch (NoSuchElementException e) {
				common.sleep(1000);
				cnt++;
			}
		}
		return false;
	}

	public void clickLinkByText(String linkTxt) {
		WebElement element = driver.findElement(By.linkText(linkTxt));
		common.waitForElementToBeClickable(element, 10);
		element.click();
		common.sleep(500);
		writeReport("Pass", "Clicked Link: " + linkTxt, false);
	}

	public void changeTimeOut(int timeToWait) {
		driver.manage().timeouts().implicitlyWait(timeToWait, TimeUnit.SECONDS);
	}

	public void changeTimeOutInMilliSeconds(int timeToWait) {
		driver.manage().timeouts().implicitlyWait(timeToWait, TimeUnit.MILLISECONDS);
	}

	public boolean validateObjectsDisplayedWODelay(WebElement element) {
		try {
			changeTimeOut(0);
			if (element != null) {
				if (element.isDisplayed() == true)
					return true;
				else
					return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			changeTimeOut(timeOut);
		}
		return false;
	}

	public boolean validateObjectsDisplayedWODelayUsingSize(By by) {
		try {
			changeTimeOut(0);
			List<WebElement> elements = driver.findElements(by);
			if (elements != null) {
				if (elements.size() > 0)
					return true;
				else
					return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			changeTimeOut(timeOut);
		}
		return false;
	}

	public boolean validateObjectsDisplayedWODelay(String xPath) {
		WebElement element;
		try {
			changeTimeOut(0);
			element = driver.findElement(By.xpath(xPath));
			if (element != null) {
				if (element.isDisplayed() == true)
					return true;
				else
					return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			changeTimeOut(timeOut);
		}
		return false;
	}

	public boolean validateObjectsDisplayedWODelay(By by) {
		WebElement element;
		try {
			changeTimeOut(0);
			element = driver.findElement(by);
			if (element != null) {
				if (element.isDisplayed() == true)
					return true;
				else
					return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			changeTimeOut(timeOut);
		}
		return false;
	}

	public boolean waitAndClick(WebElement elementToClick, WebElement elementToWait, int timeToWait) {
		boolean flag = false;
		int cnt = 0;
		while (cnt != timeToWait && flag != true) {
			try {
				javascriptClick(elementToClick);
				// elementToClick.click();
				common.sleep(2000);
				flag = common.validateObjectsDisplayedWODelay(elementToWait);
				cnt++;
				System.out.println("While looped @ " + cnt + " - " + getCurrentDate("HH:mm:ss"));
			} catch (StaleElementReferenceException e) {
				return false;
			} catch (WebDriverException e) {
				System.out.println("Element to click is not clickable or not visible at this moment");
				System.out.println("Error: " + e.getMessage());
				flag = false;
				cnt++;
			}
		}
		if (flag == false)
			writeReport("Fail", "Element is not clickable, hence exiting out of execution", true);
		return flag;
	}

	public void closeAllWindows() throws InterruptedException {
		String homeWindow = driver.getWindowHandle();
		Set<String> allWindows = driver.getWindowHandles();

		// Use Iterator to iterate over windows
		Iterator<String> windowIterator = allWindows.iterator();

		// Verify next window is available
		while (windowIterator.hasNext()) {

			// Store the Recruiter window id
			String childWindow = windowIterator.next();

			// Here we will compare if parent window is not equal to child
			// window
			if (homeWindow.equals(childWindow)) {
				driver.switchTo().window(childWindow);
				driver.close();
			}
		}
	}

	// Handling alert to either accept or dismiss
	public boolean handleAlert(boolean acceptOrDismiss) {
		Alert alert = driver.switchTo().alert();
		if (acceptOrDismiss) {
			alert.accept();
			return true;
		}
		alert.dismiss();
		return false;
	}

	// Handling alert accept with text
	public void handleAlertwithtext(String arg1) {
		Alert alert = driver.switchTo().alert();
		String Ale = alert.getText();
		if (Ale.equals(arg1)) {
			alert.accept();
			writeReport("Pass", "Alert text is displayed as per expected - " + Ale, false);
		} else
			writeReport("Fail", "Alert text is not as expected - " + arg1 + " - instead it is displayed as - " + Ale,
					true);
	}

	public void handleAlertwithcontaintext(String arg1) {
		Alert alert = driver.switchTo().alert();
		String Ale = alert.getText();
		if (Ale.contains(arg1)) {
			alert.accept();
			writeReport("Pass", "Alert text is displayed as per expected - " + Ale, false);
		} else
			writeReport("Fail", "Alert text is not as expected - " + arg1 + " - instead it is displayed as - " + Ale,
					true);
	}

	// isSelected option
	public boolean isSelected(By by) {
		return driver.findElement(by).isSelected();
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public void waitForAlert(int timeToWait) {
		WebDriverWait wait = new WebDriverWait(driver, timeToWait);
		wait.until(ExpectedConditions.alertIsPresent());
	}

	public String GenerateRandomNumber(int charLength) {
		return String.valueOf(charLength < 1 ? 0
				: new Random().nextInt((9 * (int) Math.pow(10, charLength - 1)) - 1)
						+ (int) Math.pow(10, charLength - 1));
	}

	public void simpleClick(WebElement element) {
		element.click();
		common.sleep(200);
	}

	public void writeReport(String status, String message, boolean screenShotNeeded) {
		boolean flag;
		if (status.equals("Pass"))
			flag = true;
		else
			flag = false;
		messageToPrint = message;
		Reporter.addStepLog(common.getCurrentDate("hh:mm:ss:SSS") + " - " + message);
		/*
		 * String fileName = message.replace(" ", "").replace("\"", "").replace(":",
		 * "").replace("×", "").replace("\n", "") .replace("#", "").replace("/", "");
		 */
		if (screenShotNeeded)
			writeScreenshot();
		Assert.assertTrue(flag);
	}

	public void writeReportWarning(String message) {
		message = "WARNING: " + message + " [Proceeding to next step]";
		messageToPrint = message;
		Reporter.addStepLog("<span style=\"background-color: #FF0000\">" + common.getCurrentDate("hh:mm:ss:SSS") + " - "
				+ message + "</span>");
		writeScreenshot();
	}

	public void writeScreenshot() {
		try {
			Reporter.addScreenCaptureFromPath(common.captureScreenShot());
		} catch (IOException e) {
			System.out.println("Ended with exception on capturing screenshot");
		}
	}

	public int getElementSize(String xPath) {
		return driver.findElements(By.xpath(xPath)).size();
	}

	public int getElementSize(By by) {
		return driver.findElements(by).size();
	}

	public void clickImageByAltName(String name) {
		WebElement element = driver.findElement(By.xpath("//img[@alt='" + name + "']"));
		element.click();
		sleep(500);
	}

	public void javascriptClick(WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
	}

	public void javascriptmouseover(WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].mouseOver();", element);
	}

	public void javascriptClick(WebElement element, String message) {
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", element);
			common.sleep(700);
			writeReport("Pass", "Clicked the " + message, false);
		} catch (NoSuchElementException e) {
			writeReport("Fail", "Not able to click the button " + message, true);
		}
	}

	public void javascriptClick(String xpath) {
		WebElement element = driver.findElement(By.xpath(xpath));
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
	}

	public void javascriptSendKeys(WebElement element, String text) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].value=arguments[1];", element, text);
	}

	public void waitForElementVisibility(WebElement systemMenuButton, int timeToWait) {
		WebDriverWait wait = new WebDriverWait(driver, timeToWait);
		wait.until(ExpectedConditions.visibilityOf(systemMenuButton));
	}

	public boolean validateToastMessage(String expectedMsg, String actualMsg) {
		if (expectedMsg.contains(actualMsg)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean matchString(String expectedMsg, String actualMsg) {
		if (expectedMsg.matches(actualMsg)) {
			return true;
		} else {
			return false;
		}
	}

	public String captureScreenShot() {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String encodedBase64 = null;
		FileInputStream fileInputStreamReader = null;
		try {
			fileInputStreamReader = new FileInputStream(scrFile);
			byte[] bytes = new byte[(int) scrFile.length()];
			fileInputStreamReader.read(bytes);
			encodedBase64 = new String(Base64.getEncoder().encode(bytes));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "data:image/png;base64," + encodedBase64;
	}

	public void selectDropdown(WebElement element, String name) {
		Select select = new Select(element);
		select.selectByValue(name);
		common.sleep(250);
	}

	public void selectDropdownByText(WebElement element, String name) {
		Select select = new Select(element);
		select.selectByVisibleText(name);
		common.sleep(250);
	}

	public void selectDropdownByOptions(WebElement element, String name) {
		Select select = new Select(element);
		List<WebElement> options = select.getOptions();
		for (WebElement option : options) {
			if (option.getText().contains(name)) {
				option.click();
				break;
			}
		}
		common.sleep(250);
	}

	public void selectDropdown(WebElement element, int index) {
		Select select = new Select(element);
		select.selectByIndex(index);
		common.sleep(250);
	}

	public void clickButton(String value) {
		try {
			WebElement element = driver.findElement(By.xpath("//input[@value='" + value + "']"));
			common.clickElement(element, "Continue");
			common.sleep(900);
			writeReport("Pass", "Clicked " + value + " button", false);
		} catch (NoSuchElementException e) {
			writeReport("Fail", "Not able to click the button " + value, true);
		}
	}

	public void clickcheckbox(WebElement ele, String mess) {
		try {
			ele.click();
			common.sleep(900);
			writeReport("Pass", "Checked the " + mess + " button", false);
		} catch (NoSuchElementException e) {
			writeReport("Fail", "Not able to check the button " + mess, true);
		}
	}

	public void clickButtonwithText(String value) {
		try {
			WebElement element = driver.findElement(By.xpath("//button[text()='" + value + "']"));
			// common.javascriptClick(element);
			element.click();
			common.sleep(900);
			writeReport("Pass", "Clicked " + value + " button", false);
		} catch (NoSuchElementException e) {
			writeReport("Fail", "Not able to click the button " + value, true);
		}
	}

	public void clickElement(WebElement element, String message) {
		try {
			element.click();
			common.sleep(700);
			writeReport("Pass", "Clicked the " + message, false);
		} catch (NoSuchElementException e) {
			writeReport("Fail", "Not able to click the button " + message, true);
		}
	}

	public void clickElement(String xpath, String message) {
		try {
			WebElement element = getElement(xpath);
			element.click();
			common.sleep(1000);
			writeReport("Pass", "Clicked " + message, false);
		} catch (NoSuchElementException e) {
			writeReport("Fail", "Not able to click the button " + message, true);
		}
	}

	public void actionMoveToElement(WebElement element) {
		Actions act = new Actions(driver);
		act.moveToElement(element);
		// act.build().perform();
		act.perform();
	}

	public void actionMoveToElementAndClick(WebElement element) {
		Actions act = new Actions(driver);
		act.moveToElement(element);
		act.click();
		act.build().perform();
	}

	public void actionMoveToElementOffset(WebElement element, int xOffset, int yOffset) {
		Actions act = new Actions(driver);
		act.moveToElement(element, xOffset, yOffset);
		act.click();
		act.build().perform();
	}

	public void scrollIntoView(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		common.sleep(500);
	}

	public int getTableRow(WebElement element, String csName) {
		List<WebElement> rows = element.findElements(By.tagName("tr"));
		int rowCount = rows.size();
		for (int row = 0; row < rowCount; row++) {
			List<WebElement> columnData = rows.get(row).findElements(By.tagName("td"));
			int columnCount = columnData.size();
			for (int column = 0; column < columnCount; column++) {
				String cellText = columnData.get(column).getText();
				if (cellText.equals(csName)) {
					return row + 1;
				}
			}
		}
		return 0;
	}

	public String getTableData(String tableXPath, int fromRow, int fromColumn) {
		String xpath = tableXPath + "//tr[" + fromRow + "]//td[" + fromColumn + "]";
		WebElement element = driver.findElement(By.xpath(xpath));
		return element.getText();
	}

	public WebElement getTableElement(String tableXPath, int fromRow, int fromColumn) {
		String xpath = tableXPath + "//tr[" + fromRow + "]//td[" + fromColumn + "]";
		WebElement element = driver.findElement(By.xpath(xpath));
		return element;
	}

	public int toInt(String str) {
		return Integer.parseInt(str);
	}

	public void sendKeysRobot(int key) {
		try {
			Robot robot = new Robot();
			robot.keyPress(key);
			robot.keyRelease(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void uploadFile(String path) {
		try {
			StringSelection stringSelection = new StringSelection(path);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getNumberOfLinesInFile(String path) {
		BufferedReader reader = null;
		// Initializing charCount, wordCount and lineCount to 0
		int charCount = 0;
		int wordCount = 0;
		int lineCount = 0;
		try {
			// Creating BufferedReader object
			reader = new BufferedReader(new FileReader(path));
			// Reading the first line into currentLine
			String currentLine = reader.readLine();
			while (currentLine != null) {
				// Updating the lineCount
				lineCount++;
				// Getting number of words in currentLine
				String[] words = currentLine.split(" ");
				// Updating the wordCount
				wordCount = wordCount + words.length;
				// Iterating each word
				for (String word : words) {
					// Updating the charCount
					charCount = charCount + word.length();
				}
				// Reading next line into currentLine
				currentLine = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close(); // Closing the reader
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return lineCount;
	}

	public void sendKeysWithName(String name, String value) {
		WebElement element = driver.findElement(By.name(name));
		element.sendKeys(value);
	}

	public void sendKeys(WebElement element, String value) {
		if (!value.isEmpty()) {
			element.clear();
			element.sendKeys(value);
			common.sleep(1000);
			if (!value.equals(element.getAttribute("value"))) {
				element.clear();
				for (int i = 0; i < value.length(); i++) {
					element.sendKeys(String.valueOf(value.charAt(i)));
				}
			}
			writeReport("Pass", "Entered " + value, false);
		}
	}

	public void sendKeysActions(WebElement element, String value) {
		Actions actions = new Actions(driver);
		Action action = actions.moveToElement(element).click().sendKeys(Keys.END).keyDown(Keys.SHIFT)
				.sendKeys(Keys.HOME).keyUp(Keys.SHIFT).sendKeys(value).build();
		action.perform();
		if (!value.equals(element.getAttribute("value"))) {
			sleep(2500);
			actions.moveToElement(element).click().sendKeys(Keys.END).keyDown(Keys.SHIFT).sendKeys(Keys.HOME)
					.keyUp(Keys.SHIFT).sendKeys(value).build().perform();
		}
		writeReport("Pass", "Entered " + value, false);
	}

	public void textclearActions(WebElement element) {
		Actions actions = new Actions(driver);
		Action action = actions.moveToElement(element).click().sendKeys(Keys.END).keyDown(Keys.SHIFT)
				.sendKeys(Keys.HOME).keyUp(Keys.SHIFT).sendKeys(Keys.DELETE).build();
		action.perform();
		writeReport("Pass", "Cleared the text", false);
	}

	public void waitForElementToBeClickable(WebElement element, int waitTime) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, waitTime);
			wait.until(ExpectedConditions.elementToBeClickable(element));
		} catch (TimeoutException e) {
			writeReport("Fail", "Timed out on waiting for the element", true);
		}
	}

	public void waitForElementAttributeContains(WebElement element, String attribute, String value, int waitTime) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, waitTime);
			wait.until(ExpectedConditions.attributeContains(element, attribute, value));
		} catch (TimeoutException e) {
			writeReport("Fail", "Timed out on waiting for the element", true);
		}
	}

	public void waitForElementToBeVisible(WebElement element, int waitTime) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, waitTime);
			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (TimeoutException e) {
			e.printStackTrace();
			writeReport("Fail", "Timed out on waiting for the element", true);
		} catch (StaleElementReferenceException e) {
			System.out.println("Stale Element Reference Exception occured, hence running it again");
			common.sleep(500);
			try {
				WebDriverWait wait = new WebDriverWait(driver, waitTime);
				wait.until(ExpectedConditions.visibilityOf(element));
			} catch (TimeoutException e1) {

				writeReport("Fail", "Timed out on waiting for the element", true);
			}
		}
	}

	public void switchToFrame(String nameOrId) {
		driver.switchTo().frame(nameOrId);
	}

	public void switchToFrame(WebElement element) {
		common.waitForFrameToBeVisible(element, 15);
		// driver.switchTo().frame(element);
		common.sleep(100);
	}

	public void switchToFrame(int index) {
		driver.switchTo().frame(index);
	}

	public void waitForFrameToBeVisible(WebElement element, int timeOut) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOut);
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(element));
		} catch (Exception e) {
			writeReport("Fail", "Timed out on waiting for the element", true);
		}
	}

	public void findFrameInAPage(String xPath) {
		int size = driver.findElements(By.tagName("iframe")).size();
		System.out.println("Number of frames in the page: " + size);
		for (int i = 0; i < size; i++) {
			driver.switchTo().frame(i);
			int total = driver.findElements(By.xpath(xPath)).size();
			System.out.println("Frame #" + i + " == " + total);
			driver.switchTo().defaultContent();
		}
	}

	public void findFrameSizeAndOuterHTML() {
		int size = driver.findElements(By.tagName("iframe")).size();
		System.out.println("Number of frames in the page: " + size);
		for (WebElement element : driver.findElements(By.tagName("iframe"))) {
			System.out.println(element.getAttribute("outerHTML"));
		}
	}

	public void compareText(WebElement element, String textToCompare, String msg) {
		if (!textToCompare.isEmpty()) {
			try {
				if (element.getText().trim().equals(textToCompare))
					writeReport("Pass", msg + " is displayed as \"" + textToCompare + "\"", false);
				else
					writeReport("Fail", msg + " is not displayed as \"" + textToCompare + "\" instead it displays as "
							+ element.getText(), true);
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				writeReport("Fail", msg + " is not displayed as \"" + textToCompare + "\"", true);
			}
		}
	}

	public void compareText(WebElement element, String textToCompare, String msg, String attribute) {
		if (!textToCompare.isEmpty()) {
			try {
				System.out.println("UI: " + element.getAttribute(attribute));
				System.out.println("Input: " + textToCompare);
				if (element.getAttribute(attribute).trim().equals(textToCompare.trim()))
					writeReport("Pass", msg + " is displayed as \"" + textToCompare + "\"", false);
				else
					writeReport("Fail", msg + " is not displayed as \"" + textToCompare + "\" instead it displays as "
							+ element.getAttribute(attribute), true);
			} catch (NoSuchElementException e) {
				writeReport("Fail", msg + " is not displayed as \"" + textToCompare + "\"", true);
			}
		}
	}

	public void compareText(String xpath, String textToCompare, String msg) {
		if (!textToCompare.isEmpty()) {
			try {
				WebElement element = driver.findElement(By.xpath(xpath));
				String elementTxt = element.getText();
				if (elementTxt.trim().equals(textToCompare))
					writeReport("Pass", msg + " is displayed as \"" + textToCompare + "\"", false);
				else
					writeReport("Fail", msg + " is not displayed as \"" + textToCompare + "\" instead it displayed as "
							+ elementTxt, true);
			} catch (NoSuchElementException e) {
				writeReport("Fail", msg + " is not displayed as \"" + textToCompare + "\"", true);
			}
		}
	}

	public void compareTextContains(WebElement element, String textToCompare, String msg) {
		if (!textToCompare.isEmpty()) {
			try {
				if (element.getText().trim().contains(textToCompare))
					writeReport("Pass", msg + " contains text as " + textToCompare, false);
				else
					writeReport("Fail", msg + " not contains text as " + textToCompare + " instead it displayed as "
							+ element.getText().trim(), true);
			} catch (NoSuchElementException e) {
				writeReport("Fail", msg + " not contains text as " + textToCompare, true);
			}
		}
	}

	public void compareTextWithValue(WebElement element, String textToCompare, String msg) {
		if (!textToCompare.isEmpty()) {
			try {
				if (element.getAttribute("value").trim().equals(textToCompare))
					writeReport("Pass", msg + " is displayed as " + textToCompare, false);
				else
					writeReport("Fail", msg + " is not displayed as " + textToCompare + " instead it displays as "
							+ element.getAttribute("value").trim(), true);
			} catch (NoSuchElementException e) {
				writeReport("Fail", "Exception occured: " + msg + " is not displayed as " + textToCompare, true);
			}
		}
	}

	public void compareTextWithariavaluenow(WebElement element, String textToCompare, String msg) {
		if (!textToCompare.isEmpty()) {
			try {
				if (element.getAttribute("aria-valuenow").equals(textToCompare))
					writeReport("Pass", msg + " is displayed as " + textToCompare, false);
				else
					writeReport("Fail", msg + " is not displayed as " + textToCompare + " instead it displays as "
							+ element.getAttribute("value"), true);
			} catch (NoSuchElementException e) {
				writeReport("Fail", "Exception occured: " + msg + " is not displayed as " + textToCompare, true);
			}
		}
	}

	public void equalTextOnly(String text1, String text2, String msg) {
		try {
			if (text1.trim().equals(text2))
				writeReport("Pass", msg + " is displayed as " + text1, false);
			else
				writeReport("Fail", msg + " is not displayed as " + text2 + " instead it displays as " + text1, true);
		} catch (NoSuchElementException e) {
			writeReport("Fail", msg + " is not displayed as " + text1, true);
		}
	}

	public boolean arrayContains(String[] arr, String targetValue) {
		for (String s : arr) {
			try {
				if (s.equals(targetValue))
					return true;
			} catch (NullPointerException e) {
				return false;
			}
		}
		return false;
	}

	public void dragAndDropcontactS(WebElement sourceElement, WebElement destinationElement) {
		try {
			if (sourceElement.isDisplayed() && destinationElement.isDisplayed()) {
				Actions action = new Actions(driver);
				// action.dragAndDrop(sourceElement, destinationElement).build().perform();
				action.moveToElement(sourceElement).pause(Duration.ofMillis(1500)).clickAndHold(sourceElement)
						.pause(Duration.ofMillis(1500)).moveByOffset(1, -1).moveToElement(destinationElement)
						.moveByOffset(1, -1).pause(Duration.ofMillis(1500)).release().build().perform();
			} else {
				System.out.println("Element was not displayed to drag");
			}
		} catch (StaleElementReferenceException e) {
			System.out.println("Element with " + sourceElement + "or" + destinationElement
					+ "is not attached to the page document " + e.getStackTrace());
		} catch (NoSuchElementException e) {
			System.out.println("Element " + sourceElement + "or" + destinationElement + " was not found in DOM "
					+ e.getStackTrace());
		} catch (Exception e) {
			System.out.println("Error occurred while performing drag and drop operation " + e.getStackTrace());
		}
	}

	public void dragAndDrop(WebElement sourceElement, WebElement destinationElement) {
		try {
			if (sourceElement.isDisplayed() && destinationElement.isDisplayed()) {
				Actions action = new Actions(driver);
				action.dragAndDrop(sourceElement, destinationElement).build().perform();
			} else {
				System.out.println("Element was not displayed to drag");
			}
		} catch (StaleElementReferenceException e) {
			System.out.println("Element with " + sourceElement + "or" + destinationElement
					+ "is not attached to the page document " + e.getStackTrace());
		} catch (NoSuchElementException e) {
			System.out.println("Element " + sourceElement + "or" + destinationElement + " was not found in DOM "
					+ e.getStackTrace());
		} catch (Exception e) {
			System.out.println("Error occurred while performing drag and drop operation " + e.getStackTrace());
		}
	}

	public void dragAndDropSplitUp(WebElement sourceElement, WebElement destinationElement) {
		try {
			if (sourceElement.isDisplayed() && destinationElement.isDisplayed()) {
				Actions action = new Actions(driver);
				action.moveToElement(sourceElement).pause(Duration.ofMillis(1500)).clickAndHold(sourceElement)
						.pause(Duration.ofMillis(1500)).moveByOffset(4, -4).moveToElement(destinationElement)
						.moveByOffset(4, -4).pause(Duration.ofMillis(1500)).release().build().perform();
			} else {
				System.out.println("Element was not displayed to drag");
			}
		} catch (StaleElementReferenceException e) {
			System.out.println("Element is not attached to the page document " + e.getStackTrace());
		} catch (NoSuchElementException e) {
			System.out.println("Element was not found in DOM " + e.getStackTrace());
		} catch (Exception e) {
			System.out.println("Error occurred while performing drag and drop operation " + e.getStackTrace());
		}
	}

	public void dragAndDropSplitdown(WebElement sourceElement, WebElement destinationElement) {
		try {
			if (sourceElement.isDisplayed() && destinationElement.isDisplayed()) {
				Actions action = new Actions(driver);
				action.moveToElement(sourceElement).pause(Duration.ofMillis(1500)).clickAndHold(sourceElement)
						.pause(Duration.ofMillis(1500)).moveByOffset(4, 4).moveToElement(destinationElement)
						.moveByOffset(4, 4).pause(Duration.ofMillis(1500)).release().build().perform();
			} else {
				System.out.println("Element was not displayed to drag");
			}
		} catch (StaleElementReferenceException e) {
			System.out.println("Element is not attached to the page document " + e.getStackTrace());
		} catch (NoSuchElementException e) {
			System.out.println("Element was not found in DOM " + e.getStackTrace());
		} catch (Exception e) {
			System.out.println("Error occurred while performing drag and drop operation " + e.getStackTrace());
		}
	}

	public void dragAndDropSplitright(WebElement sourceElement, WebElement destinationElement) {
		try {
			if (sourceElement.isDisplayed() && destinationElement.isDisplayed()) {
				Actions action = new Actions(driver);
				action.moveToElement(sourceElement).pause(Duration.ofMillis(1500)).clickAndHold(sourceElement)
						.pause(Duration.ofMillis(1500)).moveByOffset(20, 1).moveToElement(destinationElement)
						.moveByOffset(20, 1).pause(Duration.ofMillis(1500)).release().build().perform();

			} else {
				System.out.println("Element was not displayed to drag");
			}
		} catch (StaleElementReferenceException e) {
			System.out.println("Element is not attached to the page document " + e.getStackTrace());
		} catch (NoSuchElementException e) {
			System.out.println("Element was not found in DOM " + e.getStackTrace());
		} catch (Exception e) {
			System.out.println("Error occurred while performing drag and drop operation " + e.getStackTrace());
		}
	}

	public void dragAndDropElement(WebElement dragFrom, WebElement dragTo, int xOffset, int yOffset) throws Exception {
		System.out.println(
				"dragFrom =" + dragFrom + " dragTo = " + dragTo + "; xOffset =" + xOffset + " yOffset =" + yOffset);
		// Setup robot
		Robot robot = new Robot();
		robot.setAutoDelay(500);

		// Get size of elements
		Dimension fromSize = dragFrom.getSize();
		Dimension toSize = dragTo.getSize();
		System.out.println("----------------Size-------------------");
		System.out.println(fromSize.toString());
		System.out.println(toSize.toString());
		System.out.println("------------------------------");
		// Get centre distance
		int xCentreFrom = fromSize.width / 2;
		int yCentreFrom = fromSize.height / 2;
		int xCentreTo = toSize.width / 2;
		int yCentreTo = toSize.height / 2;
		System.out.println("--------------Centre--------------------");
		System.out.println(xCentreFrom);
		System.out.println(yCentreFrom);
		System.out.println(xCentreTo);
		System.out.println(yCentreTo);
		System.out.println("-----------------------------------");
		Point toLocation = dragTo.getLocation();
		Point fromLocation = dragFrom.getLocation();
		System.out.println("---------------Location-----------------");
		System.out.println(fromLocation.toString());
		System.out.println(toLocation.toString());
		System.out.println("-----------------------------------");
		// Make Mouse coordinate centre of element
		toLocation.x += xOffset + xCentreTo;
		toLocation.y += yOffset + yCentreTo;
		fromLocation.x += xOffset + xCentreFrom;
		fromLocation.y += yOffset + yCentreFrom;
		System.out.println("----------------Locationx,y-------------------");
		System.out.println(toLocation.x);
		System.out.println(toLocation.y);
		System.out.println(fromLocation.x);
		System.out.println(fromLocation.y);
		System.out.println("-----------------------------------");
		System.out.println(fromLocation.toString());
		System.out.println(toLocation.toString());
		System.out.println("-----------------------------------");
		// Move mouse to drag from location
		robot.mouseMove(fromLocation.x, fromLocation.y);
		// robot.mouseMove(175,250);

		// Thread.sleep(1000);
		// Click and drag
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

		// robot.mousePress(InputEvent.
		// Drag events require more than one movement to register
		// Just appearing at destination doesn't work so move halfway first
		robot.mouseMove(((toLocation.x - fromLocation.x) / 2) + fromLocation.x,
				((toLocation.y - fromLocation.y) / 2) + fromLocation.y);

		// Move to final position
		robot.mouseMove(toLocation.x, toLocation.y);
		// Drop
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

	public void dragAndDropElementactions(WebElement dragFrom, WebElement dragTo, int xOffset, int yOffset)
			throws Exception {
		System.out.println(
				"dragFrom =" + dragFrom + " dragTo = " + dragTo + "xOffset =" + xOffset + " yOffset =" + yOffset);
		// Setup robot
		Actions act = new Actions(driver);
		act.pause(500);

		// Get size of elements
		Dimension fromSize = dragFrom.getSize();
		Dimension toSize = dragTo.getSize();

		// Get centre distance
		int xCentreFrom = fromSize.width / 2;
		int yCentreFrom = fromSize.height / 2;
		int xCentreTo = toSize.width / 2;
		int yCentreTo = toSize.height / 2;

		Point toLocation = dragTo.getLocation();
		Point fromLocation = dragFrom.getLocation();
		System.out.println(fromLocation.toString());
		System.out.println(toLocation.toString());

		// Make Mouse coordinate centre of element
		toLocation.x += xOffset + xCentreTo;
		toLocation.y += yOffset + yCentreTo;
		fromLocation.x += xOffset + xCentreFrom;
		fromLocation.y += yOffset + yCentreFrom;

		System.out.println(fromLocation.toString());
		System.out.println(toLocation.toString());
		// Move mouse to drag from location
		act.moveToElement(dragFrom);
		// act.moveByOffset(fromLocation.x, fromLocation.y);
		// robot.mouseMove(175,250);

		// Thread.sleep(1000);
		// Click and drag
		act.clickAndHold(dragFrom);

		// robot.mousePress(InputEvent.
		// Drag events require more than one movement to register
		// Just appearing at destination doesn't work so move halfway first
		act.moveByOffset(((toLocation.x - fromLocation.x) / 2) + fromLocation.x,
				((toLocation.y - fromLocation.y) / 2) + fromLocation.y);

		// Move to final position
		act.moveToElement(dragTo, toLocation.x, toLocation.y);
		// Drop
		act.release(dragTo);
		act.build().perform();
	}

	public void dragAndDropByOffset(WebElement dragger, int xOffset, int yOffset) {
		try {
			if (dragger.isDisplayed()) {
				Actions action = new Actions(driver);
				action.dragAndDropBy(dragger, xOffset, yOffset).build().perform();
			} else {
				System.out.println("Element was not displayed to drag");
			}
		} catch (StaleElementReferenceException e) {
			System.out.println("Element is not attached to the page document " + e.getStackTrace());
		} catch (NoSuchElementException e) {
			System.out.println("Element was not found in DOM " + e.getStackTrace());
		} catch (Exception e) {
			System.out.println("Error occurred while performing drag and drop operation " + e.getStackTrace());
		}
	}

	public void scrollMouseWheel(int wheelAmt) {
		try {
			new Robot().mouseWheel(wheelAmt);
		} catch (AWTException e) {
			System.out.println(e.getMessage());
		}
	}

	public boolean markRadioFlag(WebElement element, String flag, String flagName) {
		if (!(flag.isEmpty())) {
			try {
				String state = element.getText();
				if (!state.equalsIgnoreCase(flag)) {
					element.click();
					common.sleep(500);
					writeReport("Pass", flagName + " flag is changed to " + flag, false);
					return true;
				} else {
					writeReport("Pass", flagName + " flag is already set to " + flag, false);
				}
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				writeReport("Fail", flagName + " flag is not changed to " + flag, true);
			} catch (StaleElementReferenceException e) {
				System.out.println("Stale Element Exception occured and hence running it again");
				try {
					String state = element.getText();
					if (!state.equalsIgnoreCase(flag)) {
						element.click();
						common.sleep(500);
						writeReport("Pass", flagName + " flag is changed to " + flag, false);
						return true;
					} else {
						writeReport("Pass", flagName + " flag is already set to " + flag, false);
					}
				} catch (NoSuchElementException e1) {
					e.printStackTrace();
					writeReport("Fail", flagName + " flag is not changed to " + flag, true);
				}
			}
		}
		return false;
	}

	public boolean markRadioFlagAriaChecked(WebElement element, String flag, String flagName) {
		int i = 0;
		if (!(flag.isEmpty())) {
			try {
				String state = element.getAttribute("aria-checked");
				if (!state.equalsIgnoreCase(flag)) {
					element.click();
					common.sleep(500);
					writeReport("Pass", flagName + " flag is changed to " + flag, false);
					return true;
				} else {
					writeReport("Pass", flagName + " flag is already set to " + flag, false);
				}
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				writeReport("Fail", flagName + " flag is not changed to " + flag, true);
			} catch (StaleElementReferenceException e) {
				if (i != 5) {
					System.out.println("Stale Element Exception occured and hence running it again: " + i);
					markRadioFlag(element, flag, flagName);
					i++;
				}
			}
		}
		return false;
	}


	public void validateObjectDisplayed(WebElement element, String nameOfElement) {
		if (isExists(element))
			writeReport("Pass", nameOfElement + " is displayed", false);
		else
			writeReport("Fail", nameOfElement + " is not displayed", true);
	}

	public void validateObjectSelected(WebElement element, String nameOfElement) {
		if (isExists(element))
			writeReport("Pass", nameOfElement + " is selected", false);
		else
			writeReport("Fail", nameOfElement + " is not selected", true);
	}

	public void validateErrorMessDisplayed(String xpath, String nameOfElement) {
		By b = By.xpath(xpath);
		if (isExists(b))
			writeReport("Pass", nameOfElement + " is displayed", false);
		else
			writeReport("Fail", nameOfElement + " is not displayed", true);
	}

	public void validateObjectEnabled(WebElement element, String nameOfElement) {
		if (isExists(element) && isEnabled(element))
			writeReport("Pass", nameOfElement + " is available and active", false);
		else
			writeReport("Fail", nameOfElement + " is not available/inactive", true);
	}

	public void validateObjectNotEnabled(WebElement element, String nameOfElement) {
		if (isExists(element) && !isEnabled(element))
			writeReport("Pass", nameOfElement + " is not available/active", false);
		else
			writeReport("Fail", nameOfElement + " is available and active, which is not as expected", true);
	}

	public void validateNotEnabled(WebElement element, String nameOfElement) {
		if (isExists(element))
			writeReport("Pass", nameOfElement + " is available and inactive", false);
		else
			writeReport("Fail", nameOfElement + " is available and active, which is not as expected", true);
	}

	public void validateElementNotEditable(WebElement element, String nameOfElement) {
		if (isExists(element) && !isEnabled(element))
			writeReport("Pass", nameOfElement + " is displayed and not editable", false);
		else
			writeReport("Fail", nameOfElement + " is not displayed or editable", true);
	}

	public void validateElementEditable(WebElement element, String nameOfElement) {
		if (isExists(element) && isEnabled(element))
			writeReport("Pass", nameOfElement + " is dispalyed and editable", false);
		else
			writeReport("Fail", nameOfElement + " is not displayed or editable", true);
	}

	public void validateEnabled(WebElement element, String nameOfElement) {
		if (isExists(element))
			writeReport("Pass", nameOfElement + " is available and active", false);
		else
			writeReport("Fail", nameOfElement + " is available and inactive, which is not as expected", true);
	}

	public void validateObjectnotEnabled(String xpath, String nameOfElement) {
		By b = By.xpath(xpath);
		if (isExists(b) && !isEnabled(b))
			writeReport("Pass", nameOfElement + " is not available/enabled", false);
		else
			writeReport("Fail", nameOfElement + " is  available and enabled, which is not as expected", true);
	}

	public void validateObjectEnabled(String xpath, String nameOfElement) {
		WebElement element = driver.findElement(By.xpath(xpath));
		if (isExists(element) && isEnabled(element))
			writeReport("Pass", nameOfElement + " is available and active", false);
		else
			writeReport("Fail", nameOfElement + " is not available/active", true);
	}

	public void validateObjectDisplayed(WebElement element, String nameOfElement, int timeToWait) {
		if (isExists(element, timeToWait))
			writeReport("Pass", nameOfElement + " is displayed", false);
		else
			writeReport("Fail", nameOfElement + " is not displayed", true);
	}

	public boolean validateObjectDisplayedOrNot(WebElement element, String nameOfElement, int timeToWait) {
		boolean elementvisible = false;
		if (isExists(element, timeToWait)) {
			writeReport("Pass", nameOfElement + " is not displayed as expected", true);
			elementvisible = true;
		} else {
			writeReport("Pass", nameOfElement + " is displayed", true);
			elementvisible = false;
		}
		return elementvisible;
	}

	public void validateObjectNotDisplayed(WebElement element, String nameOfElement) {
		if (isExists(element))
			writeReport("Fail", "<mark>" + nameOfElement + " is displayed, which is not as expected </mark>", true);
		else
			writeReport("Pass", "<mark>" + nameOfElement + " is not displayed/closed </mark>", false);
	}

	public void validateObjectNotDisplayedWODelay(String xpath, String nameOfElement) {
		By b = By.xpath(xpath);
		if (validateObjectsDisplayedWODelay(b))
			writeReport("Fail", nameOfElement + " is displayed, which is not as expected", true);
		else
			writeReport("Pass", nameOfElement + " is not displayed/closed", false);
	}

	public void validateObjectNotDisplayedWODelayCSS(String css, String nameOfElement) {
		By b = By.cssSelector(css);
		if (validateObjectsDisplayedWODelay(b))
			writeReport("Fail", nameOfElement + " is displayed, which is not as expected", true);
		else
			writeReport("Pass", nameOfElement + " is not displayed/closed", false);
	}

	public void validateObjectNotDisplayed(String xpath, String nameOfElement) {
		By b = By.xpath(xpath);
		if (isExists(b))
			writeReport("Fail", nameOfElement + " is displayed, which is not as expected", true);
		else
			writeReport("Pass", nameOfElement + " is not displayed", false);
	}

	public void validateObjectDisplayed(String xpath, String nameOfElement) {
		if (isExists(driver.findElement(By.xpath(xpath))))
			writeReport("Pass", nameOfElement + " is displayed/available/active", false);
		else
			writeReport("Fail", nameOfElement + " is not available/active", true);
	}

	public void validateDisabledObjectDisplayed(String xpath, String nameOfElement) {
		if (isExists(driver.findElement(By.xpath(xpath))))
			writeReport("Pass", nameOfElement + " is available/checked and inactive", false);
		else
			writeReport("Fail", nameOfElement + " is available/not checked and active", true);
	}

	public void validateDisabledObjectNotDisplayed(String xpath, String nameOfElement) {
		if (isExists(driver.findElement(By.xpath(xpath))))
			writeReport("Fail", nameOfElement + " is available/checked and active", true);
		else
			writeReport("Pass", nameOfElement + " is available/not checked and inactive", false);
	}

	public void validateDisabledObjectDisplayed(WebElement element, String nameOfElement) {
		if (isExists(element))
			writeReport("Pass", nameOfElement + " is available and inactive", false);
		else
			writeReport("Fail", nameOfElement + " is available and active", true);
	}

	public static String nextDayDate() {
		todayDate = LocalDate.now();
		System.out.println("Next date :: " + findNextDay(todayDate));
		return findNextDay(todayDate);
	}

	public static String prevDayDate() {
		todayDate = LocalDate.now();
		System.out.println("Next date :: " + findNextDay(todayDate));
		return findPrevDay(todayDate);
	}

	private static String findNextDay(LocalDate localdate) {
		return localdate.plusDays(1).toString();
	}

	private static String findPrevDay(LocalDate localdate) {
		return localdate.minusDays(1).toString();
	}

	public String getCurrentDate(String format) {
		DateFormat dFormat = new SimpleDateFormat(format);
		String currentDate = dFormat.format(new Date());
		return currentDate;
	}

	public String getCurrentDateAddYear(String date, String format, int Noyear) {

		DateFormat dFormat = new SimpleDateFormat(format);
		Date currentDate;
		String currentDate1 = null;
		try {
			currentDate = dFormat.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(currentDate);
			c.add(Calendar.YEAR, Noyear);
			Date currentDatePlusOne = c.getTime();
			currentDate1 = dFormat.format(currentDatePlusOne);
			return currentDate1;
		} catch (ParseException e) {
			e.printStackTrace();
			return currentDate1;
		}

	}

	public static String getCurrentDateAddDay(String date, String format, int day) {

		DateFormat dFormat = new SimpleDateFormat(format);
		Date currentDate;
		String currentDate1 = null;
		try {
			currentDate = dFormat.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(currentDate);
			c.add(Calendar.DATE, day);
			Date currentDatePlusOne = c.getTime();
			currentDate1 = dFormat.format(currentDatePlusOne);
			return currentDate1;
		} catch (ParseException e) {
			e.printStackTrace();
			return currentDate1;
		}

	}

	public String getDateInFormat(String date, String dateFormat, String formatToBeReturned) {
		String formattedDate = null;
		try {
			// Create a SimpleDateFormat object with the provided input format
			SimpleDateFormat inputFormatter = new SimpleDateFormat(dateFormat);

			// Parse the provided date string into a Date object
			Date parsedDate = inputFormatter.parse(date);

			// Create another SimpleDateFormat object with the desired output format
			SimpleDateFormat outputFormatter = new SimpleDateFormat(formatToBeReturned);

			// Format the parsed date into the desired output format
			formattedDate = outputFormatter.format(parsedDate);

			// Print the formatted date
			System.out.println("Formatted date: " + formattedDate);
		} catch (ParseException e) {
			// Handle parsing exception
			System.err.println("Error parsing date: " + e.getMessage());
		}
		return formattedDate;
	}

	public Boolean getDateInFormatchecking(String datevalue, String dateFormat) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			date = sdf.parse(datevalue);
			System.out.println(date.toString());
			String output = sdf.format(date);
			System.out.println(output);
			System.out.println(sdf.parse(output));
			if (sdf.parse(output).equals(sdf.parse(output))) {
				return true;
			}
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public int getDaysBetweenDates(Date d1, Date d2) {
		return (int) TimeUnit.MILLISECONDS.toDays(d1.getTime() - d2.getTime());
	}

	public String getStringBetweenChar(String str, char st, char en) {
		return str.replaceAll(".*\\" + st + "|\\" + en + ".*", "");
	}

	public String getStringBetweenChar(String str, String st, String en) {
		return str.replaceAll(".*\\" + st + "|\\" + en + ".*", "");
	}

	public String getStringBetweenCharRev(String str, String st, String en) {
		return str.replaceAll(".*\\" + st + "|" + en + ".*", "");
	}

	public String getStringBetweenStrings(String text, String textFrom, String textTo) {
		String result = "";
		result = text.substring(text.indexOf(textFrom) + textFrom.length(), text.length());
		result = result.substring(0, result.indexOf(textTo));
		return result;
	}

	public int extractIntFromString(String str) {
		return Integer.parseInt(str.replaceAll("[^0-9]", ""));
	}

	public void clickNoOfTime(WebElement element, int noOfTimes) {
		if (noOfTimes > 0) {
			for (int i = 0; i < noOfTimes; i++) {
				element.click();
				common.sleep(250);
			}
		}
	}

	public void clearTxtArea() throws AWTException {
		Robot robot = new Robot();
		Actions actions = new Actions(driver);
		actions.contextClick().build().perform();
		robot.keyPress(KeyEvent.VK_A);
		actions.contextClick().build().perform();
		robot.keyPress(KeyEvent.VK_T);
	}

	public void openNewWindow() {
		Robot r;
		try {
			r = new Robot();
			r.keyPress(KeyEvent.VK_CONTROL);
			r.keyPress(KeyEvent.VK_N);
			r.keyRelease(KeyEvent.VK_N);
			r.keyRelease(KeyEvent.VK_CONTROL);
		} catch (AWTException e) {
			System.out.println("Something went wrong in opening new window");
			e.printStackTrace();
		}
	}

	public void openNewWindowUsingJS() {
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver).executeScript("window.open();");
		}
	}

	public void openNewIncognitoWindow() {
		Robot r;
		try {
			r = new Robot();
			r.keyPress(KeyEvent.VK_CONTROL);
			r.keyPress(KeyEvent.VK_SHIFT);
			r.keyPress(KeyEvent.VK_N);
			r.keyRelease(KeyEvent.VK_N);
			r.keyRelease(KeyEvent.VK_SHIFT);
			r.keyRelease(KeyEvent.VK_CONTROL);
		} catch (AWTException e) {
			System.out.println("Something went wrong in opening new window");
			e.printStackTrace();
		}
	}

	public WebElement getElementWithAriaLabel(String label) {
		WebElement element = driver.findElement(By.xpath("//*[@aria-label='" + label + "']"));
		return element;
	}

	public WebElement getElementWithAriaLabel2(String label) {
		WebElement element = driver.findElement(By.xpath("(//*[@aria-label='" + label + "'])[last()-1]"));
		return element;
	}

	public WebElement getElementWithAriaLabel3(String label) {
		WebElement element = driver.findElement(By.xpath("(//*[@aria-label='" + label + "'])[last()]"));
		return element;
	}

	public WebElement getElementWithAriaLabel_1(String label) {
		WebElement element = driver.findElement(By.xpath("(//*[@aria-label='" + label + "'])[last()-1]"));
		return element;
	}

	public WebElement getElementWithName(String name) {
		WebElement element = driver.findElement(By.name(name));
		return element;
	}

	public WebElement getElementWithValue(String value) {
		WebElement element = driver.findElement(By.xpath("//*[@value='" + value + "']"));
		return element;
	}

	public WebElement getElementWithID(String id) {
		WebElement element = driver.findElement(By.id(id));
		return element;
	}

	public WebElement getElementWithText(String text) {
		WebElement element = driver.findElement(By.xpath("//*[normalize-space(text())='" + text + "']"));
		return element;
	}

	public WebElement getElementWithText_01(String text) {
		WebElement element = driver.findElement(By.xpath("(//*[normalize-space(text())='" + text + "'])[last()]"));
		return element;
	}

	public WebElement getElementWithText_02(String text) {
		WebElement element = driver.findElement(By.xpath("(//*[normalize-space(text())='" + text + "'])[last()-1]"));
		return element;
	}

	public String getText(WebElement element) {
		String text = element.getText();
		writeReport("Pass", "Printing the " + text + " field", false);
		return text;
	}

	public WebElement getRadioBtnWithTxt(String text) {
		WebElement element = driver.findElement(By.xpath("//span[text()='" + text + "']/../md-radio-button"));
		return element;
	}

	public WebElement getToggleWithTxt(String text) {
		WebElement element = driver
				.findElement(By.xpath("//span[text()='" + text + "']//parent::div/..//div[contains(@class,'toggle')]"));
		return element;
	}

	public void clickOKorCancelButton(String confirmorCancel) {
		common.javascriptClick("//button[contains(@class, '" + confirmorCancel + "-button')]");
		writeReport("Pass", "Clicked " + confirmorCancel + " button", false);
		common.sleep(1000);
	}

	public void refreshpage() {
		driver.navigate().refresh();
		common.sleep(1000);
	}

	public WebElement getElement(String xPath) {
		try {
			WebElement element = driver.findElement(By.xpath(xPath));
			return element;
		} catch (NoSuchElementException e) {
			System.out.println("WebElement not exists: " + e.getMessage());
			return null;
		}
	}

	public WebElement getElementCssSelector(String css) {
		try {
			WebElement element = driver.findElement(By.cssSelector(css));
			return element;
		} catch (NoSuchElementException e) {
			System.out.println("WebElement not exists: " + e.getMessage());
			return null;
		}
	}

	public String getCssvalue(WebElement element, String propertyName) {
		try {
			String CSSvalue = element.getCssValue(propertyName);
			return CSSvalue;
		} catch (NoSuchElementException e) {
			System.out.println("CSSvalue not exists: " + e.getMessage());
			return null;
		}
	}

	public By getBy(String xPath) {
		return By.xpath(xPath);
	}

	public By getByCSSSelector(String css) {
		return By.cssSelector(css);
	}

	public List<WebElement> getElements(String xPath) {
		List<WebElement> elements = driver.findElements(By.xpath(xPath));
		return elements;
	}

	public List<WebElement> getElementsCSSSelector(String css) {
		List<WebElement> elements = driver.findElements(By.cssSelector(css));
		return elements;
	}

	public boolean elementListContainsString(List<WebElement> elements, String search) {
		for (WebElement element : elements) {
			if (element.getText().contains(search)) {
				System.out.println("text: " + element.getText());
				return true;
			}
		}
		return false;
	}

	public int getCntElementListContainsString(List<WebElement> elements, String search) {
		int i = 0;
		for (WebElement element : elements) {
			i++;
			if (element.getText().contains(search))
				return i;
		}
		return 0;
	}

	public int gettextRowPosition(List<WebElement> elements, String search) {
		int i = 0;
		for (WebElement element : elements) {
			i++;
			if (element.getText().equals(search))
				return i;
		}
		return 0;
	}

	public WebElement getElementContainsString(List<WebElement> elements, String search) {
		for (WebElement element : elements) {
			if (element.getText().trim().contains(search))
				return element;
		}
		return null;
	}

	public boolean getElementContainsStringOrNot(List<WebElement> elements, String search) {
		boolean flag = false;
		for (WebElement element : elements) {
			if (element.getText().trim().contains(search)) {
				flag = true;
				break;
			} else
				flag = false;
		}
		return flag;
	}

	public void validateErrorMessageDisplayed(String msgToBeDisplayed) {
		common.sleep(500);
		boolean flg = elementListContainsString(
				driver.findElements(
						By.xpath("//*[@ng-message='pattern' or @ng-message='required' or @ng-message='url']")),
				msgToBeDisplayed);
		if (flg)
			writeReport("Pass", "Error message displayed as : " + msgToBeDisplayed, true);
		else
			writeReport("Fail", "Error message not displayed as : " + msgToBeDisplayed, true);
	}

	public boolean validateErrorMessageDisplayedcondition(String msgToBeDisplayed) {
		boolean flg = elementListContainsString(
				driver.findElements(
						By.xpath("//*[@ng-message='pattern' or @ng-message='required' or @ng-message='url']")),
				msgToBeDisplayed);
		return flg;
	}

	public void selectDropdownValue(WebElement valueToSelect, String value) {
		try {
			valueToSelect.click();
			common.sleep(250);
			writeReport("Pass", "Selected " + value + " from the dropdown", false);
		} catch (Exception e) {
			e.printStackTrace();
			writeReport("Fail", "Not able to select " + value + " from the dropdown", true);
		}
	}

	public boolean clickWithoutStale(WebElement element, int noOfTry) {
		boolean result = false;
		int attempts = 0;
		while (attempts < noOfTry) {
			try {
				// element.click();
				javascriptClick(element);
				common.sleep(100);
				result = true;
				break;
			} catch (StaleElementReferenceException e) {
				System.out.println("Stale Element Reference Exception occured : " + attempts);
			}
			attempts++;
		}
		return result;
	}

	public boolean clickWithoutStale(String xpath, int noOfTry) {
		boolean result = false;
		int attempts = 0;
		while (attempts < noOfTry) {
			try {
				driver.findElement(By.xpath(xpath)).click();
				common.sleep(100);
				result = true;
				break;
			} catch (StaleElementReferenceException e) {
				System.out.println("Stale Element Reference Exception occured : " + attempts);
				common.sleep(750);
			}
			attempts++;
		}
		return result;
	}

	public boolean isDisplayedWithoutStale(WebElement element, int noOfTry) {
		boolean result = false;
		int attempts = 0;
		while (attempts < noOfTry) {
			try {
				common.sleep(100);
				scrollIntoViewJS(element);
				// scrollIntoView(element);
				result = element.isDisplayed();
				break;
			} catch (StaleElementReferenceException e) {
				System.out.println("Stale Element Reference Exception occured : " + attempts);
			}
			attempts++;
		}
		return result;
	}

	public void moveToTopOfPage() {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");
	}

	public void jsScrollWindowDown() {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("scroll(0,500);");
	}

	public void jsScrollWindow(int no) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("window.scrollBy(0," + no + ")", "");
	}

	public void jsScrollWindowUp() {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("window.scrollBy(0,-500)", "");
	}

	public void scrollIntoViewJS(WebElement element, int no) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		jsScrollWindow(no);
	}

	public void scrollIntoViewJS(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public void validateCheckboxFlag(WebElement element, String flag, String msg) {
		if (!flag.isEmpty()) {
			if (flag.equals(element.getAttribute("aria-checked")))
				writeReport("Pass", msg + " checkbox is set to " + flag, false);
			else
				writeReport("Fail", msg + " checkbox is not set to " + flag, true);
		}
	}

	public void validateToggleFlag(WebElement element, String flag, String msg) {
		if (!flag.isEmpty()) {
			if (flag.equalsIgnoreCase(element.getText()))
				writeReport("Pass", msg + " is set to " + flag, false);
			else
				writeReport("Fail", msg + " is not set to " + flag, true);
		}
	}


	public int getNoOfAgentWindowsFromMap() {
		int i;
		for (i = 1; i < 10; i++) {
			if (!map.containsKey("Window_Agent_" + i)) {
				break;
			}
		}
		return (i - 1);
	}

	public String evaluateStringExpression(String expression) {
		String str = null;
		try {
			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine engine = mgr.getEngineByName("JavaScript");
			str = engine.eval(expression).toString();
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return str;
	}

	public void waitForNumberOfWindowsToBe(int noOfWindowsExpected, int timeToWait) {
		System.out.println(
				"Waiting for window #" + noOfWindowsExpected + " to be opened (Max wait time: " + timeToWait + " sec)");
		WebDriverWait wait = new WebDriverWait(driver, timeToWait);
		wait.until(ExpectedConditions.numberOfWindowsToBe(noOfWindowsExpected));
	}

	public void waitForURLtoContainText(String URLtoVerify, int timeToWait) {
		System.out.println(
				"Waiting for text \"" + URLtoVerify + "\" to be present (Max wait time: " + timeToWait + " sec)");
		WebDriverWait wait = new WebDriverWait(driver, timeToWait);
		wait.until(ExpectedConditions.urlContains(URLtoVerify));
	}

	public void waitForTexttobePresent(WebElement element, String text, int timeToWait) {
		System.out.println("Waiting for text \"" + text + "\" to be present (Max wait time: " + timeToWait + ")");
		WebDriverWait wait = new WebDriverWait(driver, timeToWait);
		wait.until(ExpectedConditions.textToBePresentInElement(element, text));
	}

	public void waitForTextNottobePresent(WebElement element, String text, int timeToWait) {
		System.out.println("Waiting for text \"" + text + "\" not to be present (Max wait time: " + timeToWait + ")");
		WebDriverWait wait = new WebDriverWait(driver, timeToWait);
		wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(element, text)));
	}

	public void waitForElememtNottobePresent(WebElement element, int timeToWait) {
		System.out.println(
				"Waiting for Element \"" + element + "\" not to be present (Max wait time: " + timeToWait + ")");
		WebDriverWait wait = new WebDriverWait(driver, timeToWait);
		wait.until(ExpectedConditions.invisibilityOf(element));
	}

	public void waitForExactTextNottobePresent(By by, String text, int timeToWait) {
		System.out.println(
				"Waiting for exact text \"" + text + "\" not to be present (Max wait time: " + timeToWait + ")");
		WebDriverWait wait = new WebDriverWait(driver, timeToWait);
		wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(by, text)));
	}

	public void copyFileUsingJcifs(String sourcePath, String destinationPath) throws IOException {
		// final NtlmPasswordAuthentication auth = new
		// NtlmPasswordAuthentication(domain, userName, password);
		// final SmbFile sFile = new SmbFile(destinationPath, auth);
		SmbFile sFile = new SmbFile(destinationPath);
		SmbFileOutputStream smbFileOutputStream = new SmbFileOutputStream(sFile);
		FileInputStream fileInputStream = new FileInputStream(new File(sourcePath));
		byte[] buf = new byte[16 * 1024 * 1024];
		int len;
		while ((len = fileInputStream.read(buf)) > 0) {
			smbFileOutputStream.write(buf, 0, len);
		}
		fileInputStream.close();
		smbFileOutputStream.flush();
		smbFileOutputStream.close();
	}

	public boolean removeAllExistingFilesJcifs(String _path) throws IOException {
		boolean removed = false;
		String directoryPath = getURLWithoutFileName(_path);
		if (!_path.isEmpty()) {
			SmbFile sFile = new SmbFile(directoryPath + "/");
			String a[] = sFile.list();
			for (int i = 0; i < a.length; i++) {
				SmbFile removeFile = new SmbFile(directoryPath + "/" + a[i]);
				if (removeFile.exists()) {
					removeFile.delete();
					removed = true;
					System.out.println("Deleted file : " + a[i]);
				}
				if (!removed) {
					System.out.println("Could not remove non-existing resource [" + _path + "].");
				}
			}
		}
		return removed;
	}

	public String getURLWithoutFileName(String url) {
		String str = url.replace(getLastBitFromUrl(url), "");
		return str.substring(0, str.length() - 1);
	}

	public String getLastBitFromUrl(final String url) {
		return url.replaceFirst(".*/([^/?]+).*", "$1");
	}

	public ArrayList<String> getColumnArrayFromDataTable(DataTable arg1, String column) {
		ListIterator<Map<String, String>> data = arg1.asMaps(String.class, String.class).listIterator();
		ArrayList<String> mylist = new ArrayList<String>();
		while (data.hasNext()) {
			Map<String, String> str = data.next();
			mylist.add(str.get(column));
		}
		return mylist;
	}

	public String subStringLastNoOfChr(String str, int lastNoOfChrToBeRemoved) {
		return str.substring(0, str.length() - lastNoOfChrToBeRemoved);
	}

	public void setAttribute(WebElement element, String attName, String attValue) {
		((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", element,
				attName, attValue);
	}

	public void setJSTextContent(WebElement element, String attValue) {
		((JavascriptExecutor) driver).executeScript("arguments[0].textContent = arguments[1];", element, attValue);
	}

	public String initDBInstance() {
		return new PropertyReader().readProperty("DBInstanceName") + ".";
	}

	public String readTxtFile(String fileName) {

		String line = null;
		String input = "";

		try {

			FileReader fileReader = new FileReader(fileName);

			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
				input = input + "\r\n" + line;
			}

			bufferedReader.close();

		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
		String input2 = input.replace(" ", "").replace("\n", "").replace("\t", "").replace("\r", "");
		System.out.println("fromUI " + input);
		return input2;
	}

	public void copyFileUsingSmbj(String sourcePath, String destnPath) {
		try {
			FileInputStream fileInputStream = new FileInputStream(new File(sourcePath));
			byte[] buf = new byte[16 * 1024 * 1024];
			int len;
			while ((len = fileInputStream.read(buf)) > 0) {
				uploadUsingSMBJ(destnPath, buf, len);
			}
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			writeReport("Fail", "Not able to access the file in the path specified : " + sourcePath, false);
		}
	}

	private void uploadUsingSMBJ(String destnPath, byte[] bytes, int len) throws IOException {
		String sharedPathDetails[] = destnPath.split("/", 5);
		String serverIP = sharedPathDetails[2];
		String sharedFolder = sharedPathDetails[3];
		String fileName = sharedPathDetails[4];
		SmbConfig cfg = SmbConfig.builder().build();
		SMBClient client = new SMBClient(cfg);
		Connection connection = client.connect(serverIP);
		Session session = connection.authenticate(new AuthenticationContext("", "".toCharArray(), ""));
		DiskShare share = (DiskShare) session.connectShare(sharedFolder);

		// this is com.hierynomus.smbj.share.File !
		com.hierynomus.smbj.share.File f = null;
		int idx = fileName.lastIndexOf("/");

		// if file is in folder(s), create them first
		if (idx > -1) {
			String folder = fileName.substring(0, idx);
			if (!share.folderExists(folder))
				share.mkdir(folder);
		}

		// I am creating file with flag FILE_CREATE, which will throw if file exists
		// already
		if (!share.fileExists(fileName)) {
			f = share.openFile(fileName, new HashSet<>(Arrays.asList(AccessMask.GENERIC_ALL)),
					new HashSet<>(Arrays.asList(FileAttributes.FILE_ATTRIBUTE_NORMAL)), SMB2ShareAccess.ALL,
					SMB2CreateDisposition.FILE_CREATE,
					new HashSet<>(Arrays.asList(SMB2CreateOptions.FILE_DIRECTORY_FILE)));
		} else {
			String error = "File with the name " + fileName + " already exists in the folder " + share.getSmbPath();
			System.out.println(error);
			writeReport("Fail", error, false);
		}
		if (f == null)
			System.out.println("No file present");
		OutputStream os = f.getOutputStream();
		os.write(bytes, 0, len);
		os.close();
		f.close();
		share.close();
		session.close();
		connection.close();
	}

	public void fluentWaitForElementDisplayed(By by, int timeOut, int pollingTime) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofMillis(timeOut))
				.pollingEvery(Duration.ofSeconds(pollingTime)).ignoring(NoSuchElementException.class);
		wait.until(new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				System.out.println("Waiting for element to be displayed (for max " + timeOut + " sec)...");
				return driver.findElement(by).isDisplayed();
			}
		});
	}

	public void fluentWaitForalert_confirmationmessageDisplayed(WebElement w, int timeOut, int pollingTime) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeOut))
				.pollingEvery(Duration.ofMillis(pollingTime)).ignoring(NoSuchElementException.class);
		wait.until(new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				System.out.println("Waiting for element to be displayed (for max " + timeOut + " sec)...");
				return w.isDisplayed();
			}
		});
	}

	public void waitForPageLoaded() {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				System.out.println("Current Window State       : "
						+ String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState")));
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};
		try {
			Thread.sleep(1000);
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(expectation);
		} catch (Throwable error) {
			Assert.fail("Timeout waiting for Page Load Request to complete.");
		}
	}


	public void selectDropdownWithAriaLabelAndOptionValue(String dropdownNm, String valueToSelect) {
		try {
			common.getElement("//select[@aria-label='" + dropdownNm + "']").click();
			common.sleep(250);
			WebElement element = driver.findElement(
					By.xpath("//select[@aria-label='" + dropdownNm + "']/option[@value='" + valueToSelect + "']"));
			// WebElement element = driver.findElement(By.xpath("//select/option[@value='" +
			// valueToSelect + "']"));
			element.click();
			common.sleep(500);
			writeReport("Pass", "Selected " + valueToSelect + " in the dropdown " + dropdownNm, false);
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			writeReport("Fail", "Not able to select " + valueToSelect + " in the dropdown " + dropdownNm, true);
		}
	}

	public void validateObjectDisabledUsingAttribute(WebElement element, String message) {
		if (element.getAttribute("disabled").contains("true")) {
			writeReport("Pass", message + " is in disabled state", false);
		} else {
			writeReport("Fail", message + " is not in disabled state", true);
		}
	}

	public void validateObjectEnabledUsingAttribute(WebElement element, String message) {
		if (element.getAttribute("disabled").contains("false")) {
			writeReport("Pass", message + " is in Enabled state", false);
		} else {
			writeReport("Fail", message + " is not in Enabled state", true);
		}
	}

	public void validateObjectActiveUsingClass(WebElement element, String message) {
		if (element.getAttribute("class").contains("active")) {
			writeReport("Pass", message + " is in active state", false);
		} else {
			writeReport("Fail", message + " is not in active state", true);
		}
	}

	public void validateObjectSelectedUsingAttribute(WebElement element, String message) {
		try {
			if (element.getAttribute("aria-selected").contains("true")) {
				writeReport("Pass", message + " is in selected state", false);
			} else {
				writeReport("Fail", message + " is not in selected state", true);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public boolean validateObjectActiveUsingClass(WebElement element) {
		if (element.getAttribute("class").contains("active"))
			return true;
		else
			return false;
	}

	public boolean compareNumberWithRange(int no1, int no2, int rangeValue) {
		for (int i = no1; i <= no1 + rangeValue; i++) {
			if (no2 == i) {
				return true;
			}
		}
		for (int i = no1; i >= no1 - rangeValue; i--) {
			if (no2 == i) {
				return true;
			}
		}
		return false;
	}

	public String getDateToSetValue(String dateToSet, String formatToReturn) {
		DateTimeFormatter format = DateTimeFormat.forPattern(formatToReturn);
		MutableDateTime dateTime;
		if (dateToSet.contains("Current")) {
			dateTime = MutableDateTime.now();
		} else if (dateToSet.contains("LA")) {
			String splitStr[] = dateToSet.split("_");
			dateTime = MutableDateTime.parse(splitStr[1], format);
			dateToSet = common.subStringLastNoOfChr(dateToSet, (splitStr[1].length() + 1));
		} else
			return dateToSet;
		String splitTimeToSet[] = dateToSet.split("\\+");
		String formatToBeAdded = common.getStringBetweenChar(splitTimeToSet[0], '(', ')');
		if (formatToBeAdded.equalsIgnoreCase("dd")) {
			if (splitTimeToSet.length > 1)
				dateTime.addDays(Integer.parseInt(splitTimeToSet[1].trim()));
		} else if (formatToBeAdded.equalsIgnoreCase("mm")) {
			if (splitTimeToSet.length > 1)
				dateTime.addMonths(Integer.parseInt(splitTimeToSet[1].trim()));
		} else if (formatToBeAdded.equalsIgnoreCase("yy")) {
			if (splitTimeToSet.length > 1)
				dateTime.addYears(Integer.parseInt(splitTimeToSet[1].trim()));
		}
		return dateTime.toString(format);
	}

	public void compareMinuteDiffWithRange(String time1, String time2, String format, int withinRange, String msg) {
		DateTimeFormatter formatDate = DateTimeFormat.forPattern(format);
		DateTime dt1 = DateTime.parse(time1, formatDate);
		DateTime dt2 = DateTime.parse(time2, formatDate);
		if (Math.abs(dt2.getMillis() - dt1.getMillis()) <= (withinRange * 60000))
			writeReport("Pass", msg + ": Time difference matches approximately between " + time1 + " and " + time2
					+ " having the range within " + withinRange + " minute(s)", false);
		else
			writeReport("Fail", msg + ": Time difference not matches between " + time1 + " and " + time2
					+ " having the range within " + withinRange + " minute(s)", true);
	}

	public boolean compareMinuteDiffWithRange(String time1, String time2, String format, int withinRange) {
		DateTimeFormatter formatDate = DateTimeFormat.forPattern(format);
		DateTime dt1 = DateTime.parse(time1, formatDate);
		DateTime dt2 = DateTime.parse(time2, formatDate);
		if (Math.abs(dt2.getMillis() - dt1.getMillis()) <= (withinRange * 60000))
			return true;
		else
			return false;
	}

	public ArrayList<Map<String, String>> convertRawDataTableToArrayList(DataTable arg1) {
		List<List<String>> data = arg1.raw();
		ArrayList<Map<String, String>> listToReturn = new ArrayList<Map<String, String>>();
		Map<String, String> mapData = new HashMap<String, String>();
		for (int i = 1; i < data.size(); i++) {
			for (int j = 0; j < data.get(0).size(); j++) {
				mapData.put(data.get(0).get(j), data.get(i).get(j));
			}
			listToReturn.add(new HashMap<String, String>(mapData));
		}
		return listToReturn;
	}

	public void compareMapWithKeys(Map<String, String> featureData, Map<String, String> tableData, String key) {
		String data1 = featureData.get(key);
		String data2 = tableData.get(key);
		if (data1.equals(data2)) {
			if (data2.isEmpty() || data2.equals(""))
				data1 = "_blank_";
			// data1 = data2.equals("") ? "<blank>" : data1;
			System.out.println(key + " is displayed as " + data1);
			writeReport("Pass", key + " is displayed as " + data1, false);
		} else
			writeReport("Fail", key + " is not displayed as " + data1 + ", instead it displayed as " + data2, true);

	}

	public String getStringLessThanGivenInt(int givenInt, String delimiter) {
		String strToReturn = null;
		for (int s = (givenInt - 1); s > 0; s--) {
			if (s != (givenInt - 1))
				strToReturn = s + delimiter + strToReturn;
			else
				strToReturn = String.valueOf(s);
		}
		return strToReturn;
	}

	public String addMinutestoCurrentTime(int minutestoadd, String timeformat) throws ParseException {

		SimpleDateFormat dateFormat = new SimpleDateFormat(timeformat);

		Date d = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MINUTE, minutestoadd);
		String newTime = dateFormat.format(cal.getTime());
		System.out.println("Added Time " + newTime);

		return newTime;

	}

	public String addMonthtoCurrentTime(int monthstoadd, String timeformat) throws ParseException {

		SimpleDateFormat dateFormat = new SimpleDateFormat(timeformat);

		Date d = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MONTH, monthstoadd);
		String newTime = dateFormat.format(cal.getTime());
		System.out.println("Added Time " + newTime);

		return newTime;

	}

	public String addDaytoCurrentTime(int dateToAdd, String timeformat) throws ParseException {

		SimpleDateFormat dateFormat = new SimpleDateFormat(timeformat);

		Date d = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DATE, dateToAdd);
		String newTime = dateFormat.format(cal.getTime());
		System.out.println("Added Time " + newTime);

		return newTime;

	}

	protected void RefreshIFrameByJavaScriptExecutor(String iFrameName) {
		((JavascriptExecutor) driver).executeScript(String
				.format("document.getElementById('{0}').src = " + "document.getElementById('{0}').src", iFrameName));
	}

	public boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}

	public String getrequiredDayName(String noofDay) {
		DateTime dt = new DateTime(new Date()).plusDays(Integer.parseInt(noofDay));
		String day = dt.dayOfWeek().getAsText().toUpperCase();
		return day;
	}

	public class TriggerEndEmail extends Thread {
		@Override
		public void run() {
			if (propertyReader.readPropertyEmail("TriggerEmail_EndOfRun").equals("Yes")) {
				sendEmail.sendFinalEmail();
			}
		}
	}

	public String getLastOpenedWindowID() {
		String lastWindowOpened = null;
		Iterator<String> iterator = driver.getWindowHandles().iterator();
		while (iterator.hasNext()) {
			lastWindowOpened = iterator.next();
		}
		return lastWindowOpened;
	}

	public boolean deleteExistingRecords(Scenario sce) {
		String featureTitle = sce.getId().split("---")[0].split("-")[0];
		int sizeBeforeAdd = featuresList.size();
		featuresList.add(featureTitle);
		int sizeAfterAdd = featuresList.size();
		if (sizeAfterAdd > sizeBeforeAdd)
			return true;
		return false;
	}

	public void clickOnElement(WebElement element, String text) {
		common.sleep(500);
		common.isExists(element);

		common.sleep(100);
		common.waitForElementToBeVisible(element, 5);
		if (common.isExists(element)) {
			common.sleep(250);

			writeReport("Pass", " " + text + " is displayed as expected", true);
			try {
				element.click();
			} catch (StaleElementReferenceException e) {
				System.out.println("Profile Img is not displayed");
			}
		} else
			writeReport("Fail", "Profile Img not displayed as expected", true);
		common.sleep(50);
	}

	public void compareTwoListOfElements(List<WebElement> expElements, String actElement) {
		List<WebElement> originalListElements = new ArrayList<WebElement>(expElements);
		List<String> originalList = new ArrayList<String>();
		for (WebElement webElement : originalListElements) {
			originalList.add(webElement.getText());
		}
		System.out.println("originalList: " + originalList);
		Collections.replaceAll(originalList, "  ", "");
		String res = originalList.stream().map(Object::toString).collect(Collectors.joining(","));
		String actaulData = res.replace("   ", "");
		System.out.println("actaulData: " + actaulData);
		System.out.println();// 1-2-3
		String actualAgentStatus = actElement;
		List<String> actualAgentStatusList = new ArrayList<String>(Arrays.asList(actualAgentStatus.split(",")));
		System.out.println("actualAgentStatus: " + actualAgentStatusList);
		String res1 = actualAgentStatusList.stream().map(Object::toString).collect(Collectors.joining(","));
		String expData = res1.replace("  ", "");
		System.out.println("expData: " + expData);
		System.out.println();
		Assert.assertEquals(actaulData, expData);
		writeReport("Pass", "Actual and Expected List are validated successfully", false);
	}

	public void changeTextFromListOfElements(List<WebElement> ele, String text) {
		try {
			List<WebElement> stausList = new ArrayList<WebElement>(ele);
			for (int i = 0; i <= stausList.size(); i++) {
				if (!stausList.isEmpty()) {
					String originalContactsText = stausList.get(i).getText();
					System.out.println("Cotacts List: " + originalContactsText);
					if (originalContactsText.contains(text)) {
						stausList.get(i).click();
						break;
					}
				}
			}
			writeReport("Pass", "Status has changed successfully from Not Ready to Ready", false);
		} catch (Exception e) {
			writeReport("Pass", "Status has not changed to Not Ready to Ready", false);
		}
	}

	public void validateTextEleIsPresent(WebElement ele, String text, String value) {
		try {
			common.waitForElementToBeVisible(ele, 20);
			String eleText = ele.getText();
			System.out.println(" " + ele + " Text " + eleText);
			common.compareText(ele, text, value);

		} catch (Exception e) {
		}
	}

	public static String oneDaysBefore() {
		String threeDaysBefore = "";
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.add(Calendar.DAY_OF_YEAR, -1);
		Date before = cal.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		threeDaysBefore = formatter.format(before);
		System.out.println(threeDaysBefore);
		return threeDaysBefore;
	}

	public static String oneDaysAfter() {
		String oneDayAfter = "";
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.add(Calendar.DAY_OF_YEAR, 1);
		Date before = cal.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		oneDayAfter = formatter.format(before);
		System.out.println("oneDayAfter: " + oneDayAfter);
		return oneDayAfter;
	}

	public static String updateForwardTime(String currentTime) {
		// String myTime = "14:10:10";
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		String newTime = null;
		Date d;
		try {
			d = df.parse(currentTime);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.MINUTE, 10);
			newTime = df.format(cal.getTime());
			System.out.println("New Time: " + newTime);
			return newTime;
		} catch (ParseException e) {
			e.printStackTrace();
			return newTime;
		}

	}

	public void waitAndClickElement(WebElement ele, String message) {
		try {
			common.sleep(300);
			common.waitForElementToBeVisible(ele, 20);
			common.waitForElementToBeClickable(ele, 10);
			common.isExists(ele);
			common.javascriptClick(ele);
			writeReport("Pass", "Clicked On " + message, false);
		} catch (NoSuchElementException e) {
			writeReport("Fail", "Not able to click the button " + message, true);
		}
	}

	public void waitAndClickElement(WebElement ele) {
		try {
			common.sleep(100);
			common.waitForElementToBeVisible(ele, 20);
			common.waitForElementToBeClickable(ele, 10);
			common.isExists(ele);
			common.javascriptClick(ele);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public String getCurrentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
		Date date = new Date();
		System.out.println(formatter.format(date));
		String currentTime = formatter.format(date);
		System.out.println(currentTime);
		return currentTime;
	}

	public static void getStringVal(String input) {
		final Pattern lastIntPattern = Pattern.compile("[^0-9]+([0-9]+)$");

		Matcher matcher = lastIntPattern.matcher(input);
		if (matcher.find()) {
			String someNumberStr = matcher.group(1);
			int lastNumberInt = Integer.parseInt(someNumberStr);
			System.out.println(lastNumberInt);
		}
	}

	public void VerifyTextisNotNull(WebElement element, String msg) {
		String textValueString = element.getText();
		if (!textValueString.isEmpty())
			writeReport("Pass", msg + " is not NULL and the value is : " + textValueString, false);
		else
			writeReport("Fail", msg + " is NULL", true);
	}

	public void compareStrings(String actual, String expected, String msg) {
		if (!actual.isEmpty()) {
			try {
				if (actual.equals(expected))
					writeReport("Pass", msg + " is displayed as \"" + expected + "\"", false);
				else
					writeReport("Fail",
							msg + " is not displayed as \"" + expected + "\" instead it displayed as " + actual, true);
			} catch (NoSuchElementException e) {
				writeReport("Fail", msg + " is not displayed as \"" + expected + "\"", true);
			}
		}
	}

	public String convertStringToMaskedString(String unmaskedString) {
		String maskedString = "";
		try {
			String[] twocharStringArray = unmaskedString.split("(?<=\\G.{2})");
			for (int i = 0; i < twocharStringArray.length; i++) {
				if (i % 2 != 0) {
					for (int j = 0; j < twocharStringArray[i].length(); j++)
						maskedString += "X";
				} else {
					maskedString += twocharStringArray[i];
				}
			}
			return maskedString;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}

	}

	public String rangeOfTime(long time) {
		long r = 15 * 60 * 1000;
		long endTime = (time / r) * r + r;
		long startTime = (time / r) * r;

		String endTimeString = new SimpleDateFormat("HH:mm").format(new Date(endTime));
		String startTimeString = new SimpleDateFormat("HH:mm").format(new Date(startTime));

		String timeRangeString = endTimeString + " - " + startTimeString;
		return timeRangeString;

	}

	public void jsScrollWindowToBottom() {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("window.scrollBy(0,document.body.scrollHeight)");
	}

	public void VerifyTextisNullOREmpty(WebElement element, String msg) {
		String textValueString = element.getText();
		if (textValueString.isEmpty())
			writeReport("Pass", msg + " is blank", false);
		else
			writeReport("Fail", msg + " is not blank", true);
	}

	public List<String> getDataIntoList(String xpath) {
		List<WebElement> elements = common.getElements(xpath);
		List<WebElement> originalListElements = new ArrayList<WebElement>(elements);
		List<String> elementsList = new ArrayList<String>();
		for (WebElement webElement : originalListElements) {
			common.waitForElementToBeVisible(webElement, 20);
			elementsList.add(webElement.getText());
		}
		return elementsList;
	}

	public void dragAndDropJS(WebElement textOption, WebElement divDroppableArea) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("function createEvent(typeOfEvent) {\n" + "var event =document.createEvent(\"CustomEvent\");\n"
				+ "event.initCustomEvent(typeOfEvent,true, true, null);\n" + "event.dataTransfer = {\n" + "data: {},\n"
				+ "setData: function (key, value) {\n" + "this.data[key] = value;\n" + "},\n"
				+ "getData: function (key) {\n" + "return this.data[key];\n" + "}\n" + "};\n" + "return event;\n"
				+ "}\n" + "\n" + "function dispatchEvent(element, event,transferData) {\n"
				+ "if (transferData !== undefined) {\n" + "event.dataTransfer = transferData;\n" + "}\n"
				+ "if (element.dispatchEvent) {\n" + "element.dispatchEvent(event);\n"
				+ "} else if (element.fireEvent) {\n" + "element.fireEvent(\"on\" + event.type, event);\n" + "}\n"
				+ "}\n" + "\n" + "function simulateHTML5DragAndDrop(element, destination) {\n"
				+ "var dragStartEvent =createEvent('dragstart');\n" + "dispatchEvent(element, dragStartEvent);\n"
				+ "var dropEvent = createEvent('drop');\n"
				+ "dispatchEvent(destination, dropEvent,dragStartEvent.dataTransfer);\n"
				+ "var dragEndEvent = createEvent('dragend');\n"
				+ "dispatchEvent(element, dragEndEvent,dropEvent.dataTransfer);\n" + "}\n" + "\n"
				+ "var source = arguments[0];\n" + "var destination = arguments[1];\n"
				+ "simulateHTML5DragAndDrop(source,destination);", textOption, divDroppableArea);
	}

	public int[] getCoordinatesOfaLetter(WebElement textElement, String letter, int index) {
		int a[] = new int[2];
		String text = textElement.getText();

		int count = 0;
		int letterIndex = 0;
		char[] charArray = letter.toCharArray();
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == charArray[0]) {
				count++;
				if (count == index) {
					letterIndex = i;
					break;
				}
			}
		}
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		String script = "var element = arguments[0];" + "var text = arguments[1];" + "var letterIndex = arguments[2];"
				+ "var range = document.createRange();" + "range.setStart(element.firstChild, letterIndex);"
				+ "range.setEnd(element.firstChild, letterIndex + 1);" + "var rect = range.getBoundingClientRect();"
				+ "return JSON.stringify({ x: rect.left + rect.width / 2, y: rect.top + rect.height / 2 });";
		String result = (String) jsExecutor.executeScript(script, textElement, text, letterIndex);

		// Parse the JSON string to extract the x and y coordinates
		String[] parts = result.split(",");
		int xCoordinate = (int) Double.parseDouble(parts[0].substring(parts[0].indexOf(":") + 1).trim());
		int yCoordinate = (int) Double
				.parseDouble(parts[1].substring(parts[1].indexOf(":") + 1).replace('}', ' ').trim());
		System.out.println(xCoordinate + " " + yCoordinate);
		a[0] = xCoordinate;
		a[1] = yCoordinate;
		return a;
	}

}
