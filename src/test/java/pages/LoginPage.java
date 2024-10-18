package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class LoginPage extends TestExecutor {

	public LoginPage() {
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//h2[text()='SignIn']")
	public WebElement signInHeader;

	@FindBy(xpath = "//input[@placeholder='E mail']")
	public WebElement emailInput;

	@FindBy(xpath = "//input[@placeholder='Password']")
	public WebElement passwordInput;

	@FindBy(id = "enterbtn")
	public WebElement enterButton;

	@FindBy(id = "btn2")
	public WebElement skipSignInButton;

	@FindBy(id = "btn1")
	public WebElement signInButton;

	@FindBy(xpath = "//label[@id='errormsg']")
	public WebElement errorMessage;
	
	@FindBy(css = "#email")
	public WebElement emailID;
	
	@FindBy(css = "#enterimg")
	public WebElement enterImg;
	
	public void login(String email, String password) {
		emailInput.clear();
		emailInput.sendKeys(email);
		passwordInput.clear();
		passwordInput.sendKeys(password);
		enterButton.click();
		writeReport("Pass", "Entered Email as " + email + " and Password as " + password, false);
	}

	public void clickSskipSignIn() {
		skipSignInButton.click();
	}

	public void clickSignIn() {
		signInButton.click();
	}
	
	public void validateErrorMessage(String error) {
		Assert.assertEquals(error, errorMessage.getText());
	}
	
	public void validateRegisterPageIsDisplayed() {
		String title = driver.getTitle();
		Assert.assertEquals("Register", title);
	}
}
