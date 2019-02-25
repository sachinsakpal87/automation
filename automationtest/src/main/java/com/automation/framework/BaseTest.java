package com.automation.framework;

import static com.automation.reports.ExtentTestManager.getTest;
import static com.automation.reports.ExtentTestManager.logError;
import static com.automation.reports.ExtentTestManager.logInfo;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.utils.URIBuilder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

import com.automation.CustomContext;
import com.automation.core.ApiWrapper;
import com.automation.environment.EnvironmentProperties;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

abstract public class BaseTest extends AbstractBaseTest {

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
			System.out.println("Hash code is " + web.get().hashCode() + "Object ref is" + web.get().toString());
		}
		return web.get();
	}

	public ApiWrapper api() {
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
		} catch (URISyntaxException | MalformedURLException e) {
			return "";
		}
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestResult iTestResult) throws IOException {
		if (iTestResult.getStatus() == ITestResult.FAILURE && web() != null) {
			takeFullScreenshot(iTestResult);
		}
		web().getDriver().quit();
	}

	private void takeFullScreenshot(ITestResult iTestResult) {
		Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(web().getDriver());
		try {
			String filePath = System.getProperty("user.dir") + "/Result/" + iTestResult.getMethod().getMethodName() + ".png";
			ImageIO.write(screenshot.getImage(), "PNG", new File(filePath));
			logInfo("Screenshot captured and placed at location :: %s", filePath);
			getTest().addScreenCaptureFromPath(filePath, iTestResult.getMethod().getMethodName());
		} catch (IOException e) {
			logError("Error occurred while capturing image.", e, null);
		}
	}

	@Deprecated
	private void takeScreenshot(ITestResult iTestResult) {
		File scrFile = ((TakesScreenshot) web.get().getDriver()).getScreenshotAs(OutputType.FILE);
		try {
			if ((scrFile.exists() || scrFile.createNewFile())) {
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
}
