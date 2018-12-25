package com.automation.framework;

import com.automation.CustomContext;
import com.automation.core.ApiWrapper;
import com.automation.environment.EnvironmentProperties;
import org.apache.http.client.utils.URIBuilder;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import static com.automation.reports.ExtentTestManager.*;

public class BaseTest extends AbstractBaseTest {

    private final CustomContext customContext;
    private final ThreadLocal<WebUser> web = new ThreadLocal<>();
    private EnvironmentProperties environmentProperties;
    private ApiWrapper apiWrapper;

    public BaseTest(CustomContext customContext) {
        this.customContext = customContext;
        environmentProperties = new EnvironmentProperties();
    }

    private CustomContext getCustomContext() {
        return customContext;
    }

    protected WebUser web() {
        if (web.get() == null) {
            web.set(WebUser.buildWebUser(getCustomContext()));
        }
        return web.get();
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult iTestResult) {
        if (iTestResult.getStatus() == ITestResult.FAILURE) {
            File scrFile = ((TakesScreenshot) web.get().getDriver()).getScreenshotAs(OutputType.FILE);
            try {
                if (scrFile.createNewFile()) {
                    String filePath = System.getProperty("user.dir") + "/Result/" + iTestResult.getMethod().getMethodName() + ".png";
                    FileUtils.copyFile(scrFile, new File(filePath));
                    logInfo("Screenshot captured and placed at location :: %s", filePath);
                    getTest().addScreenCaptureFromPath(filePath, iTestResult.getMethod().getMethodName());
                } else {
                    throw new IOException();
                }
            } catch (IOException e) {
                logError("Error occurred while capturing image.", e, null);
            }
        }
        web.get().getDriver().close();
    }

    public ApiWrapper apiRobot() {
        if (apiWrapper == null) {
            apiWrapper = new ApiWrapper(environmentProperties);
            apiWrapper.setBaseUrl(buildBaseUrl());
        }
        return apiWrapper;
    }

    private String buildBaseUrl() {
        try {
            URIBuilder builder = new URIBuilder();
            builder.setScheme(environmentProperties.getEnvironmentProperty("protocol", false));
            builder.setHost(environmentProperties.getEnvironmentProperty("host", false));
            URL url = builder.build().toURL();
            return url.toString();
        } catch (URISyntaxException e) {
            return "";
        } catch (MalformedURLException e) {
            return "";
        }
    }
}
