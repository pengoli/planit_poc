package planit_poc.pageobjects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import planit_poc.helpers.PageObject;

public class CheckOutPage extends PageObject {

	public CheckOutPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	/**
	 * PageFactory to initialize page objects.
	 * Declared Strings used for Customization of locators and extracting By paths
	 */
	
	private String labelcheckout = "//h1[text()='Checkout']";
	private String newMandatoryAddressFields = "//div[@class='edit-address']//*[contains(@data-val-required,'required')]";
	private String shippingmethodradiobtn = "//input[contains(@value,'?') and @type='radio']";
	private String paymentMethodRadioBtn = "//input[@name='paymentmethod' and @value='Payments.?']";
	private String paymentInfoLabel = "//div[@class='section payment-info']//p";
	private String orderconfirmationmessage = "//div[@class='title']/child::strong";

	@FindBy(id = "billing-address-select")
	private WebElement billingAddress;
	@FindBy(className = "edit-address")
	private WebElement editaddress;
	@FindBy(xpath = "//div[@id='billing-buttons-container']/child::input[contains(@class,'new-address-next-step-button')]")
	private WebElement newaddresscontinuebutton;
	@FindBy(xpath = "//div[@id='shipping-buttons-container']/child::input[contains(@class,'new-address-next-step-button')]")
	private WebElement shippingcontinuebutton;
	@FindBy(id = "shipping-address-select")
	private WebElement shippingaddressselect;
	@FindBy(xpath = "//div[@id='shipping-method-buttons-container']/child::input[contains(@class,'shipping-method-next-step-button')]")
	private WebElement shippingmethodcontinuebutton;
	@FindBy(xpath = "//div[@id='payment-method-buttons-container']/child::input[contains(@class,'payment-method-next-step-button')]")
	private WebElement paymentmethodcontinuebutton;
	@FindBy(xpath = "//div[@class='section payment-info']//p")
	private WebElement paymentinfolabel;
	@FindBy(xpath = "//div[@id='payment-info-buttons-container']/child::input[contains(@class,'payment-info-next-step-button')]")
	private WebElement paymentinfocontinuebutton;
	@FindBy(xpath = "//div[@id='confirm-order-buttons-container']/child::input[contains(@class,'confirm-order-next-step-button')]")
	private WebElement confirmorderbutton;
	@FindBy(xpath = "//div[@class='page checkout-page']//h1[text()='Thank you']")
	private WebElement orderconfrmheader;
	@FindBy(xpath = "//div[@class='section order-completed']//li[contains(text(),'Order number:')]")
	private WebElement ordernumber;
	@FindBy(xpath = "//input[contains(@class,'order-completed-continue-button')]")
	private WebElement ordercomplteBtn;
	@FindBy(xpath = "//a[@class='ico-logout']")
	private WebElement logoutBtn;

	public void validatePageLoaded() {
		assertExists(labelcheckout);
	}

	public void selectBillingAddress(String billingAdd) {
		Select select = new Select(billingAddress);
		select.selectByVisibleText(billingAdd);
	}

	/**
	 * Filling Mandatory New Address Fields by extracting the field NAmes from
	 * WebElements. From Input Excel data we are fetching the values based on the
	 * above fieldNames
	 * 
	 * @param newAddressDetails-->
	 *            Input DAta from Excel sheet as key set value pair
	 * @throws Exception
	 *             --> if Any MAndatory field Name doesn't present in the input data
	 *             sheet
	 */
	public void fillNewAddressMandatoryDetailsAndContinue(Map<String, String> newAddressDetails) throws Exception {

		String fieldName = null;
		// Wait until EditAdress fields are ready to edit/change
		waitForEnabled(editaddress);
		List<WebElement> newAddressMandatoryFieldElements = driver.findElements(By.xpath(newMandatoryAddressFields));

		for (WebElement editAddressElement : newAddressMandatoryFieldElements) {
			checkVisible(editAddressElement);
			// Extracting FieldName from the WebElement Attribute ID
			fieldName = editAddressElement.getAttribute("id").split("_")[1];

			// Making sure that we have all the mandatory input field values
			if (!newAddressDetails.containsKey(fieldName))
				throw new Exception("Missing value for Mandatory field: " + fieldName
						+ " from input Data while Declaring NewAddress Details");

			// Send input text after checking its type
			if (editAddressElement.getAttribute("type").trim().equals("text")) {
				editAddressElement.clear();
				editAddressElement.sendKeys(newAddressDetails.get(fieldName));
				continue;
			}

			// Select from drop down if the WebELement is of Select Tag
			if (editAddressElement.getTagName().trim().equalsIgnoreCase("Select")) {
				Select select = new Select(editAddressElement);
				select.selectByVisibleText(newAddressDetails.get(fieldName));
			}
			// click continue

		}

		// if(!newAddressDetails.containsKey("FirstName"))
		// throw new Exception("Missing value for Mandatory field FirstName while
		// Declaring NewAddress Details");
		// getElementFromLocatorPath(replaceStringWithParameters(newAddressField,"FirstName"),"xpath").sendKeys(newAddressDetails.get("FirstName"));
		//
		newaddresscontinuebutton.click();
	}

	/**
	 * Formatting Billing address with the input details
	 * @param inputData-->MAP taken from input sheet
	 * @return
	 */
	public String getBillingAddress(HashMap<String, String> inputData) {

		String[] billingAddressFields = new String[] { inputData.get("FirstName") + " " + inputData.get("LastName"),
				inputData.get("Address1"), inputData.get("City") + " " + inputData.get("ZipPostalCode"),
				inputData.get("CountryId") };
		return String.join(", ", billingAddressFields);

	}

	/**
	 * Select shipping Address
	 * @param shippingAddress
	 */
	public void selectShippingAddressAndContinue(String shippingAddress) {
		checkVisible(shippingaddressselect);
		Select select = new Select(shippingaddressselect);
		select.selectByVisibleText(shippingAddress);
		shippingcontinuebutton.click();
	}

	public void selectshippingMethodAndContinue(String shippingMethod) throws Exception {

		String shippingMethodlocator = replaceStringWithParameters(shippingmethodradiobtn, shippingMethod);
		waitForVisible(By.xpath(shippingMethodlocator));
		WebElement shippingMethodBtn = getElementFromLocatorPath(shippingMethodlocator, "xpath");
		waitForEnabled(shippingMethodBtn);
		shippingMethodBtn.click();
		shippingmethodcontinuebutton.click();
	}

	public void selectPaymentMethodAndContinue(String paymentMethod) throws Exception {

		String paymentMethodlocator = replaceStringWithParameters(paymentMethodRadioBtn, paymentMethod);
		waitForVisible(By.xpath(paymentMethodlocator));
		WebElement paymentMethodBtn = getElementFromLocatorPath(paymentMethodlocator, "xpath");
		waitForEnabled(paymentMethodBtn);
		paymentMethodBtn.click();
		paymentmethodcontinuebutton.click();
	}

	public String getPaymentInfoMessage() throws Exception {
		waitForVisible(By.xpath(paymentInfoLabel));
		return paymentinfolabel.getText().trim();
	}

	public void clickPaymentInfoContinue() {
		waitForEnabled(paymentinfocontinuebutton);
		paymentinfocontinuebutton.click();
	}

	public void clickConfirmOrderContinue() {
		waitForEnabled(confirmorderbutton);
		confirmorderbutton.click();
	}

	public String getOrderConfirmationMessage() throws Exception {
		waitForVisible(By.xpath(orderconfirmationmessage));
		return getElementFromLocatorPath(orderconfirmationmessage, "xpath").getText();
	}

	public String getOrderNumber() {
		waitForEnabled(ordernumber);
		return ordernumber.getText().trim().replaceAll("[^0-9]", "");
	}

	public void clickOrderCompleteButton() {
		waitForEnabled(ordercomplteBtn);
		ordercomplteBtn.click();
	}
}
