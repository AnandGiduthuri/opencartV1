package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.TestBase;

public class LoginwithPropertiesTest extends TestBase {
   
    @Test(groups= {"sanity","regression","master"})
    public void loginTest() {
        logger.info("-----Login Test Started-----");
        try 
        {
        // HomePage actions
        HomePage hp = new HomePage(getDriver());
        logger.info("Navigating to My Account.");
        hp.myAccountClick();
        
        logger.info("Clicking on Login option.");
        hp.loginClick();
        
        // LoginPage actions
        LoginPage lp = new LoginPage(getDriver());
        logger.info("Entering username and password from properties file.");
        
        lp.setUserName(p.getProperty("username")); // Username from properties file
        lp.setPassword(p.getProperty("password")); // Password from properties file
        
        logger.info("Submitting the login form.");
        lp.clickLogin();
        
        MyAccountPage ap=new MyAccountPage(getDriver());
        boolean myaclbl=ap.verifyMyAccountLabel();
        
        if(myaclbl)
        {
        	Assert.assertTrue(true);
        }
        else
        {
        	Assert.assertTrue(false);
        }
    }
        catch(Exception e)
        {
        	Assert.fail();
        }
        
        logger.info("-----Login Test Completed-----");
    }
}
