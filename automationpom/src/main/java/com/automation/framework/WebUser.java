package com.automation.framework;

import com.automation.CustomContext;
import lombok.Getter;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.InvocationTargetException;

public class WebUser {

    private static final String THIS_PTR_WEB_USER = "THIS_PTR_WEB_USER";
    private final WebDriver driver;
    private final CustomContext context;
    @Getter
    private final String mainWindowHandle;

    private WebUser(WebDriver driver, CustomContext context) {
        this.driver = driver;
        this.context = context;
        mainWindowHandle = driver.getWindowHandle();
    }


    public static WebUser buildWebUser(CustomContext context) {
        WebUser webUser;
        webUser = new WebUser(CustomDriverFactory.buildDriver(context), context);
        context.getTestNGContext().setAttribute(THIS_PTR_WEB_USER, webUser);
        context.setDriver(webUser.getDriver());
        return webUser;
    }

    private static WebUser getWebUser(CustomContext context) {
        return (WebUser) context.getTestNGContext().getAttribute(THIS_PTR_WEB_USER);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public WebUser openPageUrl(String url) {
        getDriver().get(url);
        return this;
    }

    public <T extends AbstractPage> T openPageUrl(Class<T> tClass) {
        getDriver().get(buildUrl(tClass));
        return navigateToPage(tClass);
    }

    public <T extends AbstractPage> T navigateToPage(Class<T> tClass) {
        try {
            return tClass.getConstructor(WebUser.class).newInstance(this);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            throw new IllegalStateException("Unable to call constructor of class " + tClass.getName() + " check if it has WebUser constructor");
        }
    }

    private String buildUrl(Class tClass) {
        return context.getTestNGParameters().getUrls().getUrl() + PageUrls.getUrl(tClass);
    }
}
