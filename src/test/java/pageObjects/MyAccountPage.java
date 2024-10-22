package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MyAccountPage extends BasePage {

	public MyAccountPage(WebDriver driver) {
		super(driver);
	}
	
	@FindBy(xpath="//h2[normalize-space()='My Account']")
	WebElement myAccountLabel;
	@FindBy(xpath="//a[@class='list-group-item'][normalize-space()='Logout']")
	WebElement btnLogout;
	
	public boolean verifyMyAccountLabel()
	{
		try
		{
		boolean myAcclbl=myAccountLabel.isDisplayed();
		return myAcclbl;
		}
		
		catch(Exception e)
		{
			return false;
		}
	}
	
	public void clickbtnLogout()
	{
		btnLogout.click();
	}
	

}
