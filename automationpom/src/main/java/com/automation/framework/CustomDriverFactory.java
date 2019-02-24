package com.automation.framework;

import com.automation.CustomContext;
import com.automation.enums.Browser;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static com.automation.reports.ExtentTestManager.logError;

class CustomDriverFactory {

    private final String WEB_DRIVER_IN_CONTEXT = "WEB_DRIVER_IN_CONTEXT";
    //	private static ThreadLocal<WebDriver> webDriverObj = null;
    private WebDriver webDriverObj = null;

    private CustomDriverFactory(CustomContext context) throws IOException {
        WebDriver webDriver;
        Browser browser = context.getTestNGParameters().getBrowser();
        webDriverObj = getLocalBrowserDriver(browser);
//		webDriver = webDriverObj.get();
        webDriverObj.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        context.getTestNGContext().setAttribute(WEB_DRIVER_IN_CONTEXT, webDriverObj);

    }

    static WebDriver buildDriver(CustomContext context) throws IOException {
        return (new CustomDriverFactory(context)).webDriverObj;
    }

    private static WebDriver getRemoteChrome() {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability("version", "");
        capabilities.setPlatform(Platform.LINUX);
        ChromeOptions options = new ChromeOptions();
        options.merge(capabilities);
        options.addArguments("--start-maximized");
        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(new URL("http://127.0.0.1:4445/wd/hub"), options);
        } catch (MalformedURLException e) {
            logError("Error occurred in remote driver", e);
        }
        return driver;
    }

    private static WebDriver getRemoteFirefox() throws MalformedURLException {
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability("version", "");
        capabilities.setPlatform(Platform.LINUX);
        capabilities.setJavascriptEnabled(true);
//		try {
//			webDriverObj = (new RemoteWebDriver(new URL("http://127.0.0.1:4446/wd/hub"), capabilities));
//		} catch (MalformedURLException e) {
//			logError("Error occurred in remote driver", e);
//		}
        return (new RemoteWebDriver(new URL("http://127.0.0.1:4446/wd/hub"), capabilities));
    }

    private static WebDriver getChromeDriver() throws IOException {
        String userDir = System.getProperty("user.dir");
//		System.setProperty("webdriver.chrome.driver", userDir + "/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        ChromeDriverService chromeDriverService = new ChromeDriverService
                .Builder()
                .usingAnyFreePort()
                .usingDriverExecutable(new File(userDir + "/chromedriver")).build();
        return new ChromeDriver(chromeDriverService, options);
    }

    private static WebDriver getFirefoxDriver() {
        String userDir = System.getProperty("user.dir");
        System.setProperty("webdriver.geckodriver.driver", userDir + "/geckodriver.exe");
        FirefoxOptions options = new FirefoxOptions(DesiredCapabilities.firefox());
        return new FirefoxDriver(options);
    }

    private static WebDriver getHtmlUnitDriver() {
        return new HtmlUnitDriver();
    }

    private WebDriver getLocalBrowserDriver(Browser browser) throws IOException {
        switch (browser.name()) {
            case "FIREFOX": {
                return getFirefoxDriver();
            }
            case "CHROME": {
                return getChromeDriver();
            }
            case "HTMLUNIT": {
                return getHtmlUnitDriver();
            }
            case "REMOTECHROME": {
                return getRemoteChrome();
            }
            case "REMOTEFIREFOX": {
                return getRemoteFirefox();
            }
            default:
                throw new IllegalArgumentException("Unsupported browser type: " + browser.toString());
        }
    }
}
