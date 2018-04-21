package com.automation.framework;

import com.automation.CustomContext;
import com.automation.enums.Browser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.TimeUnit;

class CustomDriverFactory {

    private static final String WEB_DRIVER_IN_CONTEXT = "WEB_DRIVER_IN_CONTEXT";

    static WebDriver buildDriver(CustomContext context) {
        WebDriver webDriver;
        Browser browser = context.getTestNGParameters().getBrowser();
        webDriver = getLocalBrowserDriver(browser);
        webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        context.getTestNGContext().setAttribute(WEB_DRIVER_IN_CONTEXT, webDriver);
        return webDriver;
    }

    private static WebDriver getLocalBrowserDriver(Browser browser) {
        switch (browser.name()) {
            case "FIREFOX": {
                FirefoxOptions options = new FirefoxOptions(DesiredCapabilities.firefox());
                return new FirefoxDriver(options);
            }
            case "CHROME": {
                String userDir = System.getProperty("user.dir");
                System.setProperty("webdriver.chrome.driver", userDir + "/chromedriver-mac-64bit");
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--start-maximized");
                return new ChromeDriver(options);
            }
            default:
                throw new IllegalArgumentException("Unsupported browser type: " + browser.toString());
        }
    }
}
