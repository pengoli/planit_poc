package planit_poc.helpers;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;

public class TestNgListner implements ITestListener{

	@Override
	public void onTestStart(ITestResult result) {
		
		Test testAnnotation;
		
		// Get the name of the stuff
		testAnnotation = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class);
		
		TestReport.createTestCase(testAnnotation.testName());
		TestReport.extentTest.info("Test Description: "+testAnnotation.description());
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

}
