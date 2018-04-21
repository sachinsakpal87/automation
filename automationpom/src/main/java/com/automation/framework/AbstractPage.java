package com.automation.framework;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.automation.reports.ExtentTestManager.LogInfo;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class AbstractPage<T extends AbstractPage> {

    private static final Set<Class<? extends Throwable>> IGNORE_EXCEPTIONS_WHILE_WAITING_SET = ImmutableSet.of(NotFoundException.class, ElementNotVisibleException.class, IndexOutOfBoundsException.class, NullPointerException.class, StaleElementReferenceException.class, IllegalStateException.class, new Class[]{NoSuchFrameException.class, WebDriverException.class});

    @Getter
    private final WebUser WEB_USER;

    protected AbstractPage(WebUser webUser) {
        PageFactory.initElements(webUser.getDriver(), this);
        WEB_USER = webUser;
    }

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
        checkArgument(nonNull(element), "WebElement can not be null");
        LogInfo(logMessage);
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


}
