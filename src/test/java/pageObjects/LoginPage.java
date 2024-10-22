package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage{

	public LoginPage(WebDriver driver) {
		super(driver);
		
	}
	
	
	@FindBy(xpath="//input[@id='input-email']")
	WebElement txtUserName;
	@FindBy(xpath="//input[@id='input-password']")
	WebElement txtPassWord;
	@FindBy(xpath="//input[@value='Login']")
	WebElement btnLogin;
	
	
	public void setUserName(String userName)
	{
		txtUserName.sendKeys(userName);
	}
	
	public void setPassword(String passWord)
	{
		txtPassWord.sendKeys(passWord);
	}
	public void clickLogin()
	{
		btnLogin.click();
	}
	
	
	

}
