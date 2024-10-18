package steps;

import java.util.concurrent.TimeUnit;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pages.TestExecutor;

public class LoginPageSteps extends TestExecutor {

	@Given("^The user is on login page$")
	public void the_user_is_on_login_page() {
		String URL = propertyReader.readProperty("URL");
		driver.get(URL);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		windowID = driver.getWindowHandle();
		map.put("Window_LCM", windowID);
	}

	@When("^Click Sign In button$")
	public void clickSignInButton() {
		loginPage.signInButton.click();
	}

	@When("^Enter Email input as \"([^\"]*)\"$")
	public void enterEmailInputAs(String email) {
		loginPage.emailInput.clear();
		loginPage.emailInput.sendKeys(email);
	}

	@When("^Enter Password input as \"([^\"]*)\"$")
	public void enterPasswordInputAs(String password) {
		loginPage.passwordInput.clear();
		loginPage.passwordInput.sendKeys(password);
	}

	@When("^Click ENTER button$")
	public void clickENTERButton() {
		loginPage.enterButton.click();
	}

	@Then("^Validate Error message displayed \"([^\"]*)\"$")
	public void validateErrorMessageDisplayed(String arg1) {
		loginPage.validateErrorMessage(arg1);
	}

	@Given("^Enter Email input as \"([^\"]*)\" in Main page$")
	public void enterEmailInputAsInMainPage(String arg1) {
		loginPage.emailID.clear();
		loginPage.emailID.sendKeys(arg1);
	}

	@Given("^Click ENTER button in Main page$")
	public void clickENTERButtonInMainPage() {
		loginPage.enterImg.click();
	}

	@Then("^Validate Register page is displayed$")
	public void validateRegisterPageIsDisplayed() {
		loginPage.validateRegisterPageIsDisplayed();
		
	}
}