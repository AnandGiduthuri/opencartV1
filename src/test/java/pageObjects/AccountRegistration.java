package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AccountRegistration extends BasePage {
	
	public AccountRegistration(WebDriver driver)
	{
		super(driver);
		
	}
	
	@FindBy(xpath="//input[@id='input-firstname']")
	WebElement txtFirstName;
	@FindBy(xpath="//input[@id='input-lastname']")
	WebElement txtLastName;
	@FindBy(xpath="//input[@id='input-email']")
	WebElement txtEmail;
	@FindBy(xpath="//input[@id='input-telephone']")
	WebElement txtTelephone;
	@FindBy(xpath="//input[@id='input-password']")
	WebElement txtPassword;
	@FindBy(xpath="//input[@id='input-confirm']")
	WebElement txtConfirmPassword;
	@FindBy(xpath="//input[@name='agree']")
	WebElement chkAgree;
	@FindBy(xpath="//input[@value='Continue']")
	WebElement btnContinue;
	@FindBy(xpath="//h1[normalize-space()='Your Account Has Been Created!']")
	WebElement msg;

	public void setFirstName(String fName)
	{
		
		txtFirstName.sendKeys(fName);
	}
	public void setLastName(String lName)
	{
		txtLastName.sendKeys(lName);
	}
	public void setEmail(String email)
	{
		txtEmail.sendKeys(email);
	}
	public void setPhoneNumber(String phno)
	{
		txtTelephone.sendKeys(phno);
	}
	public void setPassword(String pwd)
	{
		txtPassword.sendKeys(pwd);
	}
	public void setConfirmPassword(String cPwd)
	{
		txtConfirmPassword.sendKeys(cPwd);
	}
	public void clkAgree()
	{
		chkAgree.click();
	}
	public void clkContnue()
	{
		btnContinue.click();
	}
	
	public String getConfirmationMessage()
	{
		try {
			 
			
			return(msg.getText());
			
		}
		catch(Exception e)
		{
			return(e.getMessage());
		}
	}
}
