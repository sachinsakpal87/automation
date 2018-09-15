package com.automation.framework;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.automation.reports.ExtentTestManager.LogInfo;
import static com.automation.reports.ExtentTestManager.LogWarning;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class AbstractPage {

    private static final Set<Class<? extends Throwable>> IGNORE_EXCEPTIONS_WHILE_WAITING_SET = ImmutableSet.of(NotFoundException.class, ElementNotVisibleException.class, IndexOutOfBoundsException.class, NullPointerException.class, StaleElementReferenceException.class, IllegalStateException.class, new Class[]{NoSuchFrameException.class, WebDriverException.class});

    @Getter
    private final WebUser WEB_USER;

    protected AbstractPage(WebUser webUser) {
        WEB_USER = webUser;
//        CustomPageFactory.initElements(WEB_USER.getDriver(),this);
        PageFactory.initElements(WEB_USER.getDriver(),this);
    }

    public  WebDriver getDriver(){ return  WEB_USER.getDriver();}
    public String getPageTitle() {
        return WEB_USER.getDriver().getTitle();
    }

    protected String getCurrentUrl() {
        return WEB_USER.getDriver().getCurrentUrl();
    }

    protected String getWindowHandle() {
        return WEB_USER.getDriver().getWindowHandle();
    }

    protected Set<String> getWindowHandles() {
        return WEB_USER.getDriver().getWindowHandles();
    }

    protected <T extends AbstractPage> T loadPage(Class<T> tClass) {
        return WEB_USER.navigateToPage(tClass);
    }

    protected AbstractPage action(String logMessage, WebElement element, Consumer<WebElement> action) {
        checkArgument(isNotBlank(logMessage), "log message can not be null or empty");
        LogInfo(logMessage);
        return action(element,action);
    }

    protected AbstractPage action(String logMessage, Select element, Consumer<Select> action) {
        checkArgument(isNotBlank(logMessage), "log message can not be null or empty");
        LogInfo(logMessage);
        checkArgument(nonNull(element), "Select can not be null");
        action.accept(element);
        return this;
    }

    protected AbstractPage action(WebElement element, Consumer<WebElement> action) {
        checkArgument(nonNull(element), "WebElement can not be null");
        action.accept(element);
        return this;
    }

    protected AbstractPage wait(String logMessage, WebElement element) {
        return wait(logMessage, element, 5);
    }

    protected AbstractPage wait(String logMessage, WebElement element, int timeout) {
        return wait(logMessage, element, WebElement::isDisplayed, timeout);
    }

    protected AbstractPage wait(String logMessage, WebElement element, Function<WebElement, Boolean> condition, int timeout) {
        checkArgument(isNotBlank(logMessage), "log message can not be null or empty");
        checkArgument(nonNull(element), "WebElement can not be null");
        LogInfo(logMessage);

        new FluentWait<>(element)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoreAll(IGNORE_EXCEPTIONS_WHILE_WAITING_SET)
                .until(condition);
        return this;
    }

    protected  void moveToElement(WebElement webElement){
        action("Moving to Element", webElement, we -> new Actions(getDriver()).moveToElement(we).perform());
    }

    private void safeClick(WebElement webElement){
        action("Click",webElement, arg->arg.click());
    }
    protected void click(WebElement webElement) {
        try {
            safeClick(webElement);
        }catch (Exception te){
            LogWarning("Could not click element, attempt to move to element and click");
            moveToElement(webElement);
            safeClick(webElement);
        }
    }

}
