#Author: mdfazil30ofcl@gmail.com
#---------------------------------------------*****Scenarios Covered*****---------------------------------------------
#			--> Validate user able to login the application
#---------------------------------------------------------------------------------------------------------------------
@DemoSiteValidation
Feature: Validate user able to login to Demo site application successfully

  @DemoSite_LoginValidation
  Scenario: Demorun1: Validate user able to login successfully
    Given The user is on login page
    When Click Sign In button
    And Enter Email input as "Test"
    And Enter Password input as "Test"
    And Click ENTER button
    Then Validate Error message displayed "Invalid User Name or PassWord"

  @DemoSite_SignupValidation
  Scenario: Demorun2: Validate user able to Signup successfully
    Given The user is on login page
    And Enter Email input as "Test" in Main page
    And Click ENTER button in Main page
		Then Validate Register page is displayed