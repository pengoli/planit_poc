package planit_poc.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import planit_poc.helpers.PageObject;
import planit_poc.helpers.Settings;

public class LoginPage extends PageObject {

	public LoginPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	 /**
	 * PageFactory to initialize page objects.
	 * Declared Strings used for Customization of locators and extracting By paths
	 */
	
	@FindBy(xpath = "//a[@class='ico-login' and text()='Log in']")
	private WebElement loginbtn;
	@FindBy(xpath = "//div[@class='page-title']/child::h1")
	private WebElement loginpagetitle;
	@FindBy(css = "input#Email")
	private WebElement emailid;
	@FindBy(css = "input#Password")
	private WebElement password;
	@FindBy(xpath = "//input[@class ='button-1 login-button' and @value='Log in']")
	private WebElement loginsubmitbtn;
	private String loginButton = "//a[@class='ico-login' and text()='Log in']";

	public void navigateToLogin() {
		driver.get(Settings.getProperty("url"));
	}

	public void validatePageLoaded() {
		assertExists(loginButton);
	}

	public void clickLogin() {
		loginbtn.click();
	}

	public String getPageTitle() {
		checkVisible(loginpagetitle);
		return loginpagetitle.getText();
	}
/**
 * Perform Login Action after providing credentials
 * @param userName
 * @param passWord
 */
	public void doLogin(String userName, String passWord) {
		emailid.sendKeys(userName);
		password.sendKeys(passWord);
		loginsubmitbtn.submit();

	}

}
