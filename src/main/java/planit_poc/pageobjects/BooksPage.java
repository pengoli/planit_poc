package planit_poc.pageobjects;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import planit_poc.helpers.PageObject;

public class BooksPage extends PageObject {

	public BooksPage(WebDriver driver) {           
		super(driver); 
		PageFactory.initElements(driver, this);
		}
	
	/**
	 * PageFactory to initialize page objects.
	 * Declared Strings used for Customization of locators and extracting By paths
	 */
	@FindBy(css="input.qty-input")
	public WebElement itemquantity;
	@FindBy(xpath="//div[@class='product-price']/child::span[@itemprop='price']")
	public WebElement itemprice;
	@FindBy(xpath="//input[@class='button-1 add-to-cart-button' and @value='Add to cart']")
	public WebElement addtocartBtn;
	@FindBy(xpath="//div[@class='bar-notification success']/child::p")
	public WebElement successmsgelement;
	@FindBy(xpath="//div[contains(@class,'totals')][text()='Sub-Total: ']//strong")
	public WebElement itemSubtotal;
	@FindBy(xpath="//button[contains(@class,'checkout')]")
	public WebElement chekcoutBtn;
	@FindBy(xpath="//input[contains(@id,'termsofservice')]")
	public WebElement termsofserviceCheckbox;
	
	private String successMessageAlert="//div[@class='bar-notification success']/child::p";
	private String labelBooks = "//div[@class='page-title']/child::h1[text() ='Books']";
	private String booknamelink="//h2[@class='product-title']/child::a[text()='?']";
	private String selectedbookheader = "//h1[@itemprop='name' and normalize-space(text()='?')]";
	
	public void validatePageLoaded() {
		assertExists(labelBooks);
	}
	
	public void selectBook(String bookName) {
		String booknamelinkXpath=replaceStringWithParameters(booknamelink,bookName);
	driver.findElement(By.xpath(booknamelinkXpath)).click();
	}
	
	public String getPriceValue() {
		return itemprice.getText();
	}
	
	public void clearAndEnterQuantity(String quantity) {
		itemquantity.clear();
		itemquantity.sendKeys(quantity);
	}
	
	public void clickAddToCart() {
		addtocartBtn.click();
	}
	
	public void validateSelectedBookDisplayed(String bookName) {
		assertExists(replaceStringWithParameters(selectedbookheader,bookName));
	}
	
	/**
	 * returns the Message alert generated to compare with the expected
	 * @return message displayed
	 * @throws Exception
	 */
	public String getSucssesMessage() throws Exception {
		waitForVisible(By.xpath(successMessageAlert),60);
		return successmsgelement.getAttribute("innerText");
	}
	
}
