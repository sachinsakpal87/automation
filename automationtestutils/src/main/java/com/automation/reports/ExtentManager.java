package com.automation.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

class ExtentManager {
    private static final String extentReportFile = System.getProperty("user.dir") + "/Result/extentReportFile.html";
    private static ExtentReports extentReports;

    public synchronized static ExtentReports getReporter() {

        if (extentReports == null) {
            ExtentHtmlReporter extentHtmlReporter = new ExtentHtmlReporter(extentReportFile);
            extentHtmlReporter.loadXMLConfig(System.getProperty("user.dir") + "/extent-config.xml");
            extentReports = new ExtentReports();
            extentReports.attachReporter(extentHtmlReporter);
        }
        return extentReports;
    }
}
