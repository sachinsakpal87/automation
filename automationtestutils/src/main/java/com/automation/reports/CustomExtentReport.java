package com.automation.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.apache.commons.lang3.StringUtils;
import org.testng.ITestContext;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkArgument;

@Deprecated
public class CustomExtentReport {
    private static final String THIS_PTR_CUSTOM_EXTENT_REPORT = "THIS_PTR_CUSTOM_EXTENT_REPORT";
    private final ExtentReports extentReports = new ExtentReports();
    private final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private final ThreadLocal<Logger> consoleLogger = new ThreadLocal<>();

    private CustomExtentReport(ITestContext iTestContext) {
        String extentReportFile = System.getProperty("user.dir") + "/report.html";
        ExtentHtmlReporter extentHtmlReporter = new ExtentHtmlReporter(extentReportFile);
//        extentHtmlReporter.loadXMLConfig(System.getProperty("user.dir") + "/extent-config.xml");
        extentReports.attachReporter(extentHtmlReporter);
        iTestContext.setAttribute(THIS_PTR_CUSTOM_EXTENT_REPORT, this);
    }

    static CustomExtentReport buildCustomExtentReport(ITestContext iTestContext) {
        checkArgument(Objects.nonNull(iTestContext), "ITestContext value is null");
        return new CustomExtentReport(iTestContext);
    }

    public void createTest(String className, String message) {
        checkArgument(StringUtils.isNotBlank(className), "Class name should not be empty or null");
        checkArgument(StringUtils.isNotBlank(message), "Message should not be empty or null");
        extentTest.set(extentReports.createTest(className, message));
        consoleLogger.set(Logger.getLogger("Console Logger :: "));
    }

    public void LogDebug(String message, String... params) {
        checkArgument(StringUtils.isNotBlank(message), "Message should not be empty or null");
        consoleLogger.get().log(Level.FINE, String.format(message, params));
        extentTest.get().log(Status.DEBUG, String.format(message, params));
    }

    public void LogInfo(String message, String... params) {
        checkArgument(StringUtils.isNotBlank(message), "Message should not be empty or null");
        consoleLogger.get().log(Level.INFO, String.format(message, params));
        extentTest.get().log(Status.INFO, String.format(message, params));
    }

    public void LogWarning(String message, String... params) {
        checkArgument(StringUtils.isNotBlank(message), "Message should not be empty or null");
        consoleLogger.get().log(Level.WARNING, String.format(message, params));
        extentTest.get().warning(String.format(message, params));
    }

    public void LogError(String message, Throwable throwable, String... params) {
        checkArgument(Objects.nonNull(throwable), "throwable should not be empty or null");
        checkArgument(StringUtils.isNotBlank(message), "throwable should not be empty or null");
        consoleLogger.get().log(Level.SEVERE, String.format(message, params) + "\n" + throwable.getMessage());
        extentTest.get().log(Status.ERROR, String.format(message, params));
        extentTest.get().fail(throwable);
    }

    public void flush() {
        extentReports.flush();
    }

}


//    public CustomExtentReport getReport(ITestContext iTestContext){
//        return (CustomExtentReport) iTestContext.getAttribute(THIS_PTR_CUSTOM_EXTENT_REPORT);
//    }
