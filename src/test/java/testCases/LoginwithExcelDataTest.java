package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.TestBase;
import utilities.DataProviders;

public class LoginwithExcelDataTest extends TestBase {

	@Test(dataProvider = "LoginData", dataProviderClass = DataProviders.class)
	public void verifyLogin(String email, String pwd, String Status) {
		logger.info("-----Login Test Started-----");

		try {
			// HomePage actions
			HomePage hp = new HomePage(getDriver());
			logger.info("Navigating to My Account.");
			hp.myAccountClick();

			logger.info("Clicking on Login option.");
			hp.loginClick();

			// LoginPage actions
			LoginPage lp = new LoginPage(getDriver());
			logger.info("Entering username: " + email + " and password." + pwd);

			lp.setUserName(email); // Username from Excel data
			lp.setPassword(pwd);   // Password from Excel data

			logger.info("Submitting the login form.");
			lp.clickLogin();
		

			// Verify My Account Page is displayed or not
			MyAccountPage ap = new MyAccountPage(getDriver());
			boolean myaclbl = ap.verifyMyAccountLabel();

			// Handling valid and invalid logins
			if (Status.equalsIgnoreCase("valid")) {
				if (myaclbl) {
					logger.info("Login successful with valid credentials.");
					Assert.assertTrue(true, "Test passed with valid credentials.");
					ap.clickbtnLogout();
				
				} else {
					logger.error("Login failed with In valid credentials.");
					Assert.assertTrue(false, "Test failed with valid credentials.");
				}
			} else if (Status.equalsIgnoreCase("invalid")) {
				if (!myaclbl) {
					logger.info("Login failed as expected with invalid credentials.");
					Assert.assertTrue(true, "Test failed with invalid credentials.");
				} else {
					logger.error("Login passed with Invalid credentials.");
					Assert.assertTrue(true, "Test passed with invalid credentials.");
				}
			}

		} catch (Exception e) {
			logger.error("Test case failed due to an exception: " + e.getMessage());
			Assert.fail("Test case failed due to an exception.");
		}
	}
}
