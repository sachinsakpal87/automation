package com.automation.reports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static com.automation.reports.ExtentManager.getReporter;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class ExtentTestManager {
    public static final String RESULT_PATH = System.getProperty("user.dir") + "/Result/";
    private static final Map<Long, ExtentTest> extentTestMap = new HashMap<>();
    private static final Logger consoleLogger = Logger.getLogger("Logger Initialized");

    public static synchronized ExtentTest getTest() {
        return extentTestMap.get(Thread.currentThread().getId());
    }

    public static synchronized ExtentTest startTest(String testName, String desc) {
        ExtentTest test = getReporter().createTest(testName, desc);
        consoleLogger.log(Level.INFO, "Logger Initialized");
        extentTestMap.put(Thread.currentThread().getId(), test);
        return test;
    }

    public static synchronized void logDebug(String message) {
        logDebug(message, "");
    }

    public static synchronized void logDebug(String message, String... params) {
        checkArgument(StringUtils.isNotBlank(message), "Message should not be empty or null.");
        consoleLogger.log(Level.FINE, String.format(message, params));
        if (Objects.nonNull(getTest())) {
            getTest().log(Status.DEBUG, String.format(message, params));
        }
    }

    public static synchronized void logInfo(String message) {
        logInfo(message, "");
    }

    public static synchronized void logInfo(String message, String... params) {
        checkArgument(StringUtils.isNotBlank(message), "Message should not be empty or null.");
        consoleLogger.log(Level.INFO, String.format(message, params));
        if (Objects.nonNull(getTest())) {
            getTest().log(Status.INFO, String.format(message, params));
        }
    }

    public static synchronized void logWarning(String message, String... params) {
        checkArgument(StringUtils.isNotBlank(message), "Message should not be empty or null.");
        consoleLogger.log(Level.WARNING, String.format(message, params));
        if (Objects.nonNull(getTest())) {
            getTest().warning(String.format(message, params));
        }
    }

    public static synchronized void logError(String message, String... params) {
        logError(message, null, params);
    }

    public static synchronized void logError(String message, Throwable throwable, String... params) {
        checkArgument(StringUtils.isNotBlank(message), "Message should not be empty or null.");
        if (Objects.isNull(throwable)) {
            consoleLogger.log(Level.SEVERE, String.format(message, params));
            if (Objects.nonNull(getTest())) {
                getTest().log(Status.FAIL, String.format(message, params));
            }
        } else {
            consoleLogger.log(Level.SEVERE, String.format(message, params), throwable);
            if (Objects.nonNull(getTest())) {
                getTest().log(Status.FAIL, String.format(message, throwable, params));
            }
        }
    }

    public static synchronized void logPass(String message, String... params) {
        checkArgument(StringUtils.isNotBlank(message), "Message should not be empty or null.");
        consoleLogger.log(Level.INFO, String.format(message, params));
        if (Objects.nonNull(getTest())) {
            getTest().pass(String.format(message, params));
        }
    }

    public static synchronized void logFail(String message, Throwable throwable, String... params) {
        checkArgument(StringUtils.isNotBlank(message), "Message should not be empty or null.");
        if (Objects.isNull(throwable)) {
            consoleLogger.log(Level.SEVERE, () -> String.format(message, params));
            if (Objects.nonNull(getTest())) {
                getTest().fail(String.format(message, params));
            }
        } else {
            consoleLogger.log(Level.SEVERE, throwable, () -> String.format(message, params));
            if (Objects.nonNull(getTest())) {
                getTest().fail(String.format(message, throwable, params));
            }
        }
    }

    public static synchronized void logSkip(String message, String... params) {
        logSkip(message, null, params);
    }

    public static synchronized void logSkip(String message, Throwable throwable, String... params) {
        checkArgument(StringUtils.isNotBlank(message), "Message should not be empty or null.");
        if (Objects.isNull(throwable)) {
            consoleLogger.log(Level.SEVERE, () -> String.format(message, params));
            if (Objects.nonNull(getTest())) {
                getTest().skip(String.format(message, params));
            }
        } else {
            consoleLogger.log(Level.SEVERE, throwable, () -> String.format(message, params));
            if (Objects.nonNull(getTest())) {
                getTest().skip(String.format(message, throwable, params));
            }
        }
    }

    public static synchronized void addScreenShot(String filePath) throws IOException {
        checkArgument(StringUtils.isNotBlank(filePath), "file path cannot be Empty or null" + filePath);
        addScreenShot(filePath, "");
    }

    public static synchronized void addScreenShot(String filePath, String title) throws IOException {
        checkArgument(StringUtils.isNotBlank(filePath), "file path cannot be Empty or null" + filePath);
        getTest().addScreenCaptureFromPath(filePath, title);
    }

    public static synchronized void flush() {
        checkNotNull(getReporter(), "ExtentReport is not set");
        getReporter().flush();
    }

    public static synchronized void saveLogs(String testName) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        FileHandler fh;
        try {
            FileUtils.deleteDirectory(new File(RESULT_PATH));
            File logDir = new File(RESULT_PATH + "logs/");
            if (!(logDir.exists())) {
                logDir.mkdirs();
            }

            fh = new FileHandler(RESULT_PATH + "logs/" + dateFormat.format(date) + "_" + testName.replace(" ", "_") + ".log");
            fh.setFormatter(new SimpleFormatter());
            fh.setLevel(Level.ALL);
            consoleLogger.addHandler(fh);
        } catch (IOException throwable) {
            consoleLogger.log(Level.SEVERE, throwable, () -> "Error while creating log file ");
        }
    }
}
