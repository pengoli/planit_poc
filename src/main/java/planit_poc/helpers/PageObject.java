package planit_poc.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageObject {

	// The default timeout is 60 seconds.
	protected int defaultTimeout = 60;

	protected WebDriver driver;

	protected PageObject(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Method to get web element based on the type of locator and path 
	 * @param locatorpath
	 * @param typeOfLocator
	 * @return
	 */
	
	protected WebElement getElementFromLocatorPath(String locatorpath, String typeOfLocator) {
		switch (typeOfLocator.trim().toUpperCase()) {
		case "XPATH":
			return driver.findElement(By.xpath(locatorpath));
		case "ID":
			return driver.findElement(By.id(locatorpath));
		case "CSS":
			return driver.findElement(By.cssSelector(locatorpath));
		case "className":
			return driver.findElement(By.className(locatorpath));
		default:
			return driver.findElement(By.xpath(locatorpath));
		}
	}

	protected boolean waitForEnabled(WebElement element) {
		return this.waitForEnabled(element, defaultTimeout);
	}

	/**
	 * Wait for the element to be clickable until specified timeout
	 * @param element WebElement which need to be checked
	 * @param timeout
	 * @return
	 */
	
	protected boolean waitForEnabled(WebElement element, int timeout) {
		// String message = "Wait for element selected " + by.toString() + " to be
		// enabled.";

		try {
			new WebDriverWait(driver, timeout).until(ExpectedConditions.elementToBeClickable(element));

			return true;

		} catch (Throwable t) {
			String error = "waitForClickable: Element selected by " + element.toString() + " is not enabled after "
					+ Integer.toString(timeout) + " second(s).";
			TestReport.extentTest.fail(error);
		}
		return false;
	}

	/**
	 * Wait for the element to be visible 
	 * @param by
	 * @throws Exception
	 */
	
	protected void waitForVisible(By by) throws Exception {
		this.waitForVisible(by, defaultTimeout);
	}

	/**
	 * Wait for the element to be visible for specified timeout
	 * @param by
	 * @param timeout
	 * @throws Exception
	 */
	protected void waitForVisible(By by, int timeout) throws Exception {

		try {
			new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(by));

		} catch (Throwable t) {
			String error = "waitForVisible: Element selected by " + by.toString() + " is not visible after "
					+ Integer.toString(timeout) + " second(s).";
			throw new Exception(error);
		}
	}
    
	/**
	 * Check for the visibility of the element
	 * @param element WebElement
	 * @return boolean
	 */
	protected boolean checkVisible(WebElement element) {
		return this.checkVisible(element, defaultTimeout);
	}
	
	/**
	 * Check for the visibility of the element for the specified timeout
	 * @param element WebElement
	 * @param timeout
	 * @return boolean
	 */
	
	protected boolean checkVisible(WebElement element, int timeout) {
		try {
			new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOf(element));
			return true;

		} catch (Throwable t) {
			return false;
		}

	}

	/**
	 * Method to call  AssertExists method with defaultTimeout
	 * @param element
	 */
	protected void assertExists(String element) {
		this.assertExists(element, defaultTimeout);
	}
/**
 * Check for the Element is present 
 * @param element--> element string path  
 * @param timeout-->TimeLimit before raisining exception
 */
	protected void assertExists(String element, int timeout) {
		try {
			new WebDriverWait(driver, timeout).until(ExpectedConditions.presenceOfElementLocated(By.xpath(element)));

		} catch (TimeoutException t) {
			String error = "assertExists: Element selected by " + element.toString() + " could not be found on screen.";
			throw new RuntimeException(error);
		}

	}

	/**
	 * Method is used for dynamic locators where we will replace the dynamic string in place of '?'
	 * @param actual--> Actual String
	 * @param parameters--> Parameters which need to be replaced dynamically in the place of '?'
	 * @return
	 */
	protected String replaceStringWithParameters(String actual, String... parameters) {
		for (String parameter : parameters) {
			actual = actual.replaceFirst("\\?", parameter);
		}
		return actual;

	}

}
