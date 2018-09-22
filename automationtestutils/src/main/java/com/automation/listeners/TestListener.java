package com.automation.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import static com.automation.reports.ExtentTestManager.*;


public class TestListener implements ITestListener {

    private static String getMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getMethodName();
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        saveLogs(iTestContext.getName());
        LogInfo("Starting Execution");
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        LogInfo("Test Execution Completed");
        LogInfo("Generating Results");
        flush();
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        startTest(iTestResult.getTestClass().getXmlTest().getName(), "Initializing Test :: " + getMethodName(iTestResult));
        LogInfo("Started :: %s", iTestResult.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult itestResult) {
        LogPass("Test case passed :: %s", getMethodName(itestResult));
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        if (iTestResult.getThrowable() != null) {
            LogFail("Test is failed %s", iTestResult.getThrowable(), getMethodName(iTestResult));
        } else {
            LogFail("Test is failed %s", null, getMethodName(iTestResult));
        }
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        if (iTestResult.getThrowable() != null) {
            LogSkip("Test case Skipped :: %s", iTestResult.getThrowable().getMessage(), getMethodName(iTestResult));
        } else {
            LogSkip("Test case Skipped :: %s", getMethodName(iTestResult));
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        if (iTestResult.getThrowable() != null) {
            LogFail("Test is failed %s", iTestResult.getThrowable(), getMethodName(iTestResult));
        } else {
            LogFail("Test is failed %s", null, getMethodName(iTestResult));
        }
    }
}
