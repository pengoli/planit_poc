package planit_poc.helpers;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class TestReport {

	// Flag if the report has been initialized already
	public static boolean initialized = false;

	public static String htmlReportDir;
	public static ExtentReports extent;
	public static ExtentHtmlReporter htmlReporter;
	public static ExtentTest extentTest;

	/**
	 * Will initialize extent reports at the path provided at config.properties
	 */
	public synchronized static void initializeReport() {

		// Do not initialize twice
		if (initialized) {
			return;
		}

		// Prepare folders
		prepareFolders();

		// Get the report dir
		htmlReportDir = Settings.getProperty("report_output_path");

		// Create the HTML reporter
		htmlReporter = new ExtentHtmlReporter(
				Settings.getProperty("report_output_path") + File.separator + "report.html");

		// Initialize extent reports in the given path
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);

		// Mark as initialized
		initialized = true;
	}

	/**
	 * Will create the reports folder in case it does not exist.
	 */
	private static void prepareFolders() {
		File directory;

		// Check if the test folder exists
		directory = new File(
				Settings.getProperty("report_output_path") + File.separator + "report_data" + File.separator);
		if (directory.exists()) {

			// Clean the directory
			try {
				FileUtils.cleanDirectory(directory);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		// Create the screenshots directory
		new File(Settings.getProperty("report_output_path") + File.separator + "report_data").mkdirs();
	}

	/**
	 * Will create a new test in the main report and return it to Test Class.
	 * 
	 * @param testName
	 *            Name of the test case.
	 * @return An instance of ExtentTest.
	 */
	public static void createTestCase(String testName) {
		// Make sure we initialize the report before moving on
		if (!initialized) {
			initializeReport();
		}

		// Return the new test case
		extentTest = extent.createTest(testName);

	}

	/**
	 * AddScreenshot to the Report File.
	 * 
	 * @param driver
	 *            --> WebDriver
	 * @param detailMessage
	 *            --> Message to be displayed on the screen
	 */
	public static void addScreenshot(WebDriver driver, String detailMessage) {

		File screenshotFile;
		File reportFile;
		// Time stamp helps setting an unique name for the file
		String timestamp = Long.toString(System.currentTimeMillis());

		// We need one path for saving the file and one for the report.
		String fileName = htmlReportDir + File.separator + "report_data" + File.separator + "screenshot-" + timestamp
				+ ".png";
		try {
			// Take a screenshot from the web driver
			screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			reportFile = new File(fileName);
			FileUtils.copyFile(screenshotFile, reportFile);
			extentTest.addScreenCaptureFromPath(reportFile.getAbsolutePath(), detailMessage);

		} catch (Exception e) {
			return;
		}
	}

	/**
	 * Will clears the report
	 */
	public synchronized static void flush() {
		if (!initialized) {
			return;
		}
		extent.flush();
	}

}
