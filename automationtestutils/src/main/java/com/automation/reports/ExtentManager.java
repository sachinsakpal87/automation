package com.automation.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

class ExtentManager {
	private static final String EXTENT_REPORT_FILE = System.getProperty("user.dir") + "/Result/extentReportFile.html";
	private static ExtentReports extentReports;

	private ExtentManager() {
		throw new UnsupportedOperationException();
	}

	public synchronized static ExtentReports getReporter() {

		if (extentReports == null) {
			ExtentHtmlReporter extentHtmlReporter = new ExtentHtmlReporter(EXTENT_REPORT_FILE);
			extentHtmlReporter.loadXMLConfig(System.getProperty("user.dir") + "/extent-config.xml");
			extentReports = new ExtentReports();
			extentReports.attachReporter(extentHtmlReporter);
		}
		return extentReports;
	}
}
