package planit_poc.tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.codoid.products.exception.FilloException;

import planit_poc.helpers.DriverProperties;
import planit_poc.helpers.FileUtils;
import planit_poc.helpers.Settings;
import planit_poc.helpers.TestNgListner;
import planit_poc.helpers.TestReport;
import planit_poc.pageobjects.BooksPage;
import planit_poc.pageobjects.CheckOutPage;
import planit_poc.pageobjects.HomePage;
import planit_poc.pageobjects.LoginPage;
import planit_poc.pageobjects.ShoppingCartPage;

/**
 * Author: Prashanth Jain Engoli 
 * Description: Test Class Validates Order Confirmation of Book items from WebShop Application
 */
@Listeners(TestNgListner.class)
public class WebShopPlaceBooksOrder {
	public WebDriver driver;

	@BeforeMethod
	public void beforeMethod() throws IOException {
		Settings.loadProperties();
		driver = DriverProperties.intializeWebdriver();
		TestReport.initializeReport();
	}

	/**
	 * will return each data row from input excel data as Map to the test method
	 * @return
	 * @throws FilloException
	 */
	@DataProvider
	public Object[][] getInputData() throws FilloException {

		return FileUtils.readInputExcelData(Settings.getProperty("input_path") + File.separator + "OrderDetails.xlsx");

	}

	@Test(testName = "Validate Placing Book Order functionalty", description="Login To WebShop Application and order the Book Item by providing shipping address, billing address and payment method details.",dataProvider = "getInputData")
	public void ValidatePlacingBooksOrderFunctionality(HashMap<String, String> inputData) throws Exception {
		// Declaring local parameters and storing input data parameters
		double expSubTotal, actSubTotal;
		String userName = inputData.get("UserName").trim();
		String password = inputData.get("Password").trim();
		String bookName = inputData.get("ItemToBeSelected").trim();
		String quantity = inputData.get("Quantity").trim();
		String billingAdd = inputData.get("BilingAddress").trim();
		String shippingMethod = inputData.get("ShippingMethod").trim();
		String shippingAddress = inputData.get("ShippingAddress").trim();
		String paymentMethod = inputData.get("PaymentMethod").trim();

		// Declaring messages which need to be validated
		String expWelcomeMessage = "Welcome, Please Sign In!";
		String expSuccessmsg = "The product has been added to your shopping cart";
		String expPaymentInfo = "You will pay by COD";
		String expOrderConfirmationMessage = "Your order has been successfully processed!";
		String completeBillingAddress;

		// Initialization of page classes
		LoginPage loginPage = new LoginPage(driver);
		HomePage homePage = new HomePage(driver);
		BooksPage booksPage = new BooksPage(driver);
		ShoppingCartPage shoppingCartPage = new ShoppingCartPage(driver);
		CheckOutPage checkoutPage = new CheckOutPage(driver);
		
		/* Test Case Steps */
		// 1. Login And validate the PageLoad
		loginPage.navigateToLogin();
		loginPage.validatePageLoaded();
		TestReport.extentTest.pass("Navigate To Login Page and validate");

		// 2.Click on Login Button
		loginPage.clickLogin();

		// 3. Validate expected WelcomeMessage
		Assert.assertEquals(expWelcomeMessage, loginPage.getPageTitle().trim());

		// 4.Login with credentials
		loginPage.doLogin(userName, password);
		
		// 5.validate AccountId exists on top right of the page
		homePage.validateAccountIdExists();
		TestReport.extentTest.pass("Login and Verify Navigate To Home Page completed");
		
		// 6.Clear the shopping cart if items exist in the cart
		if (homePage.isItemsExistInCart()) {
			homePage.clickShoppingCart();
			shoppingCartPage.validatePageLoaded();
			shoppingCartPage.clearShoppingCart();
			shoppingCartPage.validateShoppingCartEmptyMessgDisplayed();
			TestReport.extentTest.pass("Clear the shopping cart and validate the message displayed as : 'Your Shopping Cart is empty!'");
		}
		// 7.select books from categories
		homePage.clickBooksCategory();
		
		// 8.Select Book 'Fiction' from the list
		booksPage.validatePageLoaded();
		booksPage.selectBook(bookName);
		booksPage.validateSelectedBookDisplayed(bookName);
		TestReport.extentTest.pass("Navigate to books page and select book type as : " +bookName);
		
		
		// 9.Get the price details and enter more than one in quantity
		String price = booksPage.getPriceValue();
		booksPage.clearAndEnterQuantity(quantity);
		
		// 10.click on add to cart
		booksPage.clickAddToCart();
		TestReport.extentTest.pass("Get price details and enter quantity of : " +quantity +" and click on add to cart button");
		
		// 11.validate the success message
		Assert.assertEquals(booksPage.getSucssesMessage().trim(), expSuccessmsg);
		TestReport.extentTest.pass("Validate Success message" +expSuccessmsg+ "is displayed on the screen");
		
		// 12. click on shopping cart
		homePage.clickShoppingCart();
		shoppingCartPage.validatePageLoaded();
		
		// Validate the sub total of selectedItem
		expSubTotal = Double.valueOf(price) * Double.valueOf(quantity);
		actSubTotal = Double.valueOf(shoppingCartPage.getSubTotalPrice());
		Assert.assertEquals(actSubTotal, expSubTotal, 0);
		TestReport.extentTest.pass("Click on shopping cart and validate the actual subtotal"+actSubTotal+ "and expected subtotal" +expSubTotal+ "are equal");

		// 13.click on checkout and validatePAgeLoaded
		shoppingCartPage.clickCheckOutButton();
		checkoutPage.validatePageLoaded();

		// 14.Select Billing Address from Drop down
		checkoutPage.selectBillingAddress(billingAdd);

		// 15.Fill All Mandatory Fields and click continue
		checkoutPage.fillNewAddressMandatoryDetailsAndContinue(inputData);
		TestReport.extentTest.pass("Fill all new address data fields");

		// 16. Select Shipping Address same as Billing Address
		if (shippingAddress.equals("Billing Address")) {
			completeBillingAddress = checkoutPage.getBillingAddress(inputData);
			System.out.println(completeBillingAddress);
			checkoutPage.selectShippingAddressAndContinue(completeBillingAddress);
			TestReport.extentTest.pass("Select Shipping address same as Billing address which is :  " +completeBillingAddress);
		} else
			checkoutPage.selectShippingAddressAndContinue(shippingAddress);

		// 17.Select Shipping method and click continue
		checkoutPage.selectshippingMethodAndContinue(shippingMethod);
		TestReport.extentTest.pass("Select Shipping method as : " +shippingMethod);

		// 18.Choose the Payment method and click on continue
		checkoutPage.selectPaymentMethodAndContinue(paymentMethod);
		TestReport.extentTest.pass("Select Payment method as : " +paymentMethod);

		// 19.validate the payment information message
		Assert.assertEquals(checkoutPage.getPaymentInfoMessage(), expPaymentInfo);
		checkoutPage.clickPaymentInfoContinue();
		TestReport.extentTest.pass("Validate payment information message : " +expPaymentInfo+ " displayed on the screen and click on continue button");

		// 20.click on confirm order
		checkoutPage.clickConfirmOrderContinue();

		// 21.validate order confirmation message and print order number
		Assert.assertEquals(checkoutPage.getOrderConfirmationMessage().trim(), expOrderConfirmationMessage);
		String orderNumber = checkoutPage.getOrderNumber();
		System.out.println("Order Number is :" + orderNumber);
		TestReport.extentTest.pass("Validate order confirmation message as" +expOrderConfirmationMessage+ "is displayed on the screen with ORDER NUMBER as : " +orderNumber);
		TestReport.addScreenshot(driver, "Order Confirmation Page");
		checkoutPage.clickOrderCompleteButton();

		// 22.Logout from the application
		homePage.validateAccountIdExists();
		homePage.clickLogout();
		loginPage.validatePageLoaded();
		TestReport.extentTest.pass("click on logout button and validate application is logged out successfully");
		
	}

	@AfterMethod
	public void teardown(ITestResult result) {
		if(ITestResult.FAILURE==result.getStatus()) {
			TestReport.addScreenshot(driver, "Test Case Failed");
		TestReport.extentTest.fail("Test Case Failed with exception : "+result.getThrowable().toString());}
		driver.quit();
	}
	@AfterTest
	public void tearDown() {
		TestReport.flush();
		if(driver!=null)
		driver.quit();
	}

}
