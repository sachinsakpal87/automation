package com.automation.framework;

import com.automation.CustomContext;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

import java.io.File;
import java.io.IOException;

import static com.automation.reports.ExtentTestManager.*;

public class BaseTest extends AbstractBaseTest {

    private final CustomContext customContext;
    private final ThreadLocal<WebUser> web = new ThreadLocal<>();

    public BaseTest(CustomContext customContext) {
        this.customContext = customContext;
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
                scrFile.createNewFile();
                String filePath = System.getProperty("user.dir") + "/Result/" + iTestResult.getMethod().getMethodName() + ".png";
                FileUtils.copyFile(scrFile, new File(filePath));
                LogInfo("Screenshot captured and placed at location :: %s", filePath);
                getTest().addScreenCaptureFromPath(filePath, iTestResult.getMethod().getMethodName());
            } catch (IOException e) {
                LogError("Error occurred while capturing image.", e, null);
            }
        }
        web.get().getDriver().close();
    }
}
