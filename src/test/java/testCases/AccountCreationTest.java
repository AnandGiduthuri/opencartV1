package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.AccountRegistration;
import pageObjects.HomePage;
import testBase.TestBase;

public class AccountCreationTest extends TestBase {
	
	private static final String EXPECTED_CONFIRMATION_MSG = "Your Account Has Been Created!";

	@Test(groups = { "sanity", "master" })
	public void accountRegiTest() {
		logger.info("-----Account Registration Started-----");
		try {
			HomePage hp = new HomePage(getDriver());
			hp.myAccountClick();
			logger.info("-------Test Case Started with click on Account Link-------");
			hp.registerClick();
			logger.info("-------click on Register Link-------");
			AccountRegistration accreg = new AccountRegistration(getDriver());
			accreg.setFirstName(randomeString());
			accreg.setLastName(randomeString());
			accreg.setEmail(randomeString() + "@gmail.com");
			accreg.setPhoneNumber(randomeNumber());
			String passWord = randomAlphaNumeric();
			accreg.setPassword(passWord);
			accreg.setConfirmPassword(passWord);
			accreg.clkAgree();
			logger.info("-------Registration page details filled-------");
			accreg.clkContnue();
			logger.info("-------Click on Continue in registration and validating confirmation message-------");
			String ConfirmationMsg = accreg.getConfirmationMessage();
			logger.info("-------****Confirmation Message Page****--------");
			Assert.assertEquals(ConfirmationMsg, EXPECTED_CONFIRMATION_MSG,
					"Account creation confirmation message mismatch");

		} catch (Exception e) {
			logger.error("Test case failed due to an exception: " + e.getMessage());
			Assert.fail("Test case failed: " + e.getMessage());
		}
	}
}
