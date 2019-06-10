package planit_poc.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import planit_poc.helpers.PageObject;

public class ShoppingCartPage extends PageObject {

	public ShoppingCartPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	/**
	 * PageFactory to initialize page objects.
	 * Declared Strings used for Customization of locators and extracting By paths
	 */
	private String removeCartItems = "//td[@class='remove-from-cart']/child::input[@name='removefromcart' and @type='checkbox']";
	private String labelshoppingcart = "//h1[text()='Shopping cart']";
	private String labelshoppingcartempty = "//div[@class='order-summary-content' and normalize-space(text()='Your Shopping Cart is empty!')]";

	@FindBy(xpath = "//input[@value='Update shopping cart']")
	private WebElement updateshoppingcart;
	@FindBy(xpath = "//span[text()='Sub-Total:']/../following-sibling::td//span[@class='product-price']")
	private WebElement subtotalprice;
	@FindBy(css = "button#checkout")
	private WebElement checkoutbutton;
	@FindBy(css = "input#termsofservice")
	private WebElement termsofservicecheckbox;
	
	public void validatePageLoaded() {
		assertExists(labelshoppingcart);
	}

	/**
	 * Clears all the cart items.
	 */
	public void clearShoppingCart() {

		List<WebElement> elements = driver.findElements(By.xpath(removeCartItems));
		for (WebElement element : elements) {
			if (!element.isSelected())
				element.click();
		}
		updateshoppingcart.click();

	}

	public void validateShoppingCartEmptyMessgDisplayed() {
		assertExists(labelshoppingcartempty);
	}

	public String getSubTotalPrice() {
		return subtotalprice.getText();
	}

	public void clickCheckOutButton() {
		if(!termsofservicecheckbox.isSelected())
			termsofservicecheckbox.click();
		checkoutbutton.click();
	}
}
