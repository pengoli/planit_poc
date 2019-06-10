package planit_poc.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import planit_poc.helpers.PageObject;

public class HomePage extends PageObject {

	WebDriver driver;

	public HomePage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	/**
	 * PageFactory to initialize page objects.
	 * Declared Strings used for Customization of locators and extracting By paths
	 */
	
	@FindBy(xpath = "//li[@id='topcartlink']/child::a[@href='/cart']")
	private WebElement shoppingcartbtn;
	@FindBy(xpath = "//a[@href ='/cart']/child::span[@class='cart-qty']")
	private WebElement shoppingcartqty;
	@FindBy(xpath = "//a[text()='Log out']")
	private WebElement logout;
	@FindBy(xpath = "//ul[@class='top-menu']/child::li/child::a[@href ='/books']")
	private WebElement booksmenu;
	private String accountId = "//div[@class='header-links']//a[@href='/customer/info']";

	public void validateAccountIdExists() {
		assertExists(accountId);
	}

	public void clickShoppingCart() {
		waitForEnabled(shoppingcartbtn);
		shoppingcartbtn.click();
	}
/**
 * check if any items exists in the cart after login
 * @return true if exists 
 */
	public boolean isItemsExistInCart() {
		String quantityDisplayed = shoppingcartqty.getText();
		int quantity = Integer.valueOf(quantityDisplayed.replaceAll("[^0-9]", ""));
		if (quantity > 0)
			return true;
		return false;
	}

	public void clickBooksCategory() {
		booksmenu.click();
	}
	
	public void clickLogout() {
		logout.click();
	}

}
