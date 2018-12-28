package com.automation.framework;

import com.automation.reports.ExtentTestManager;
import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Locatable;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.automation.reports.ExtentTestManager.logWarning;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class AbstractPage {

    private static final Set<Class<? extends Throwable>> IGNORE_EXCEPTIONS_WHILE_WAITING_SET = ImmutableSet.of(NotFoundException.class, ElementNotVisibleException.class, IndexOutOfBoundsException.class, NullPointerException.class, StaleElementReferenceException.class, IllegalStateException.class, new Class[]{NoSuchFrameException.class, WebDriverException.class});
    private static final String LOG_MESSAGE_CAN_NOT_BE_NULL_OR_EMPTY = "log message can not be null or empty";

    @Getter
    private final WebUser webUser;

    protected AbstractPage(WebUser webUser) {
        this.webUser = webUser;
        PageFactory.initElements(this.webUser.getDriver(), this);
    }

    private WebDriver getDriver() {
        return webUser.getDriver();
    }

    private void safeClick(WebElement webElement) {
        checkNotNull(webElement, "WebElement can not be null");
        wait("Wail till element displayed ", webElement, 5);
        action("Click", webElement, arg -> arg.click(), this);
    }

    public String getPageTitle() {
        return getDriver().getTitle();
    }

    protected String getBrowserUrl() {
        return getDriver().getCurrentUrl();
    }

    protected String getWindowHandle() {
        return getDriver().getWindowHandle();
    }

    protected Set<String> getWindowHandles() {
        return getDriver().getWindowHandles();
    }

    protected void refresh() {
        getDriver().navigate().refresh();
    }

    protected WebElement findElement(By element) {
        return getDriver().findElement(element);
    }

    protected List<WebElement> findElements(By element) {
        return getDriver().findElements(element);
    }

    protected <T extends AbstractPage> T loadPage(Class<T> tClass) {
        return webUser.navigateToPage(tClass);
    }

    protected <T extends AbstractPage> T action(String logMessage, WebElement element, Consumer<WebElement> action, T page) {
        checkArgument(isNotBlank(logMessage), LOG_MESSAGE_CAN_NOT_BE_NULL_OR_EMPTY);
        ExtentTestManager.logInfo(logMessage);
        return action(element, action, page);
    }

    protected <T extends AbstractPage> T action(WebElement webElement, Consumer<WebElement> action, T page) {
        checkNotNull(webElement, "WebElement can not be null");
        action(webElement, action);
        return page;
    }

    protected AbstractPage action(String logMessage, WebElement webElement, Consumer<WebElement> action) {
        checkArgument(isNotBlank(logMessage), LOG_MESSAGE_CAN_NOT_BE_NULL_OR_EMPTY);
        checkNotNull(webElement, "WebElement can not be null");
        ExtentTestManager.logInfo(logMessage);
        return action(webElement, action);
    }

    protected AbstractPage action(WebElement webElement, Consumer<WebElement> action) {
        checkNotNull(webElement, "WebElement can not be null");
        action.accept(webElement);
        return this;
    }

    protected <T extends AbstractPage> T action(String logMessage, Select element, Consumer<Select> action, T page) {
        checkArgument(isNotBlank(logMessage), LOG_MESSAGE_CAN_NOT_BE_NULL_OR_EMPTY);
        checkNotNull(element, "Select can not be null");
        action(logMessage, element, action);
        return page;
    }

    protected AbstractPage action(String logMessage, Select element, Consumer<Select> action) {
        checkArgument(isNotBlank(logMessage), LOG_MESSAGE_CAN_NOT_BE_NULL_OR_EMPTY);
        checkNotNull(element, "Select can not be null");
        ExtentTestManager.logInfo(logMessage);
        return action(element, action);
    }

    protected AbstractPage action(Select element, Consumer<Select> action) {
        checkNotNull(element, "Select can not be null");
        action.accept(element);
        return this;
    }

    protected WebElement wait(String logMessage, WebElement element, int timeout) {
        return wait(logMessage, ExpectedConditions.visibilityOf(element), 5);
    }

    protected AbstractPage wait(String logMessage, WebElement webElement, Function<WebElement, Boolean> condition, int timeout) {
        checkArgument(isNotBlank(logMessage), LOG_MESSAGE_CAN_NOT_BE_NULL_OR_EMPTY);
        checkNotNull(webElement, "WebElement can not be null");
        ExtentTestManager.logInfo(logMessage);

        new FluentWait<>(webElement)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoreAll(IGNORE_EXCEPTIONS_WHILE_WAITING_SET)
                .until(condition);
        return this;
    }

    protected WebElement wait(String logMessage, ExpectedCondition<WebElement> condition, int timeout) {
        checkArgument(isNotBlank(logMessage), LOG_MESSAGE_CAN_NOT_BE_NULL_OR_EMPTY);
        ExtentTestManager.logInfo(logMessage);
        return new FluentWait<>(getDriver())
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoreAll(IGNORE_EXCEPTIONS_WHILE_WAITING_SET)
                .until(condition);
    }

    protected AbstractPage moveToElement(WebElement webElement) {
        checkNotNull(webElement, "WebElement can not be null");
        return action("Moving to Element", webElement, we -> new Actions(getDriver()).moveToElement(we).perform(), this);
    }

    protected Alert switchToAlert() {
        return switchToAlert(5);
    }

    protected Alert switchToAlert(int timeout) {
        return new FluentWait<>(getDriver())
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoreAll(IGNORE_EXCEPTIONS_WHILE_WAITING_SET)
                .until(driver -> driver.switchTo().alert());
    }

    protected AbstractPage switchToWindow(String windowName) {
        checkArgument(StringUtils.isNotBlank(windowName),"Window name can not be null or blank");
        return switchToWindow(windowName, 5);
    }

    protected AbstractPage switchToWindow(String windowName, int timeout) {
        checkArgument(StringUtils.isNotBlank(windowName),"Window name can not be null or blank");
        new FluentWait<>(getDriver())
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoreAll(IGNORE_EXCEPTIONS_WHILE_WAITING_SET)
                .until(driver -> driver.switchTo().window(windowName));
        return this;
    }

    protected AbstractPage switchToiFrame(String frameName) {
        checkArgument(StringUtils.isNotBlank(frameName),"Frame name can not be null or blank");
        return switchToiFrame(frameName, 5);
    }

    protected AbstractPage switchToiFrame(String frameName, int timeout) {
        checkArgument(StringUtils.isNotBlank(frameName),"Frame name can not be null or blank");
        new FluentWait<>(getDriver())
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoreAll(IGNORE_EXCEPTIONS_WHILE_WAITING_SET)
                .until(driver -> driver.switchTo().frame(frameName));
        return this;
    }

    /**
     * Need to update this method add lambda etc
     */
    protected AbstractPage switchToMainWindow() {
        String mainWindowHandle = getWebUser().getMainWindowHandle();
        Set<String> handles = getDriver().getWindowHandles();
        Iterator<String> itr = handles.iterator();
        while (itr.hasNext()) {
            String curr = itr.next();
            if (!curr.equals(mainWindowHandle)) {
                getDriver().switchTo().window(curr).close();
            }
        }
        getDriver().switchTo().window(mainWindowHandle);
        return this;
    }

    protected void dragAndDrop(WebElement source, WebElement destination) {
        checkNotNull(source, "Source WebElement can not be null");
        checkNotNull(destination, "Destination WebElement can not be null");
        action("Drag and Drop", source, we -> new Actions(getDriver()).dragAndDrop(source, destination).perform());
    }

    /**
     * @deprecated (when, can use dragAndDrop source destination, keeping for reference...)
     */
    @Deprecated
    protected void dragAndDrop(WebElement source, WebElement destination, boolean old) {
        checkNotNull(source, "Source WebElement can not be null");
        checkNotNull(destination, "Destination WebElement can not be null");
        action("click and hold", source, we -> new Actions(getDriver()).clickAndHold(source));
        moveToElement(destination);
        action("release", source, we -> new Actions(getDriver()).release().perform());
    }

    /**
     * @deprecated (when, can use dragAndDrop source destination, keeping for reference...)
     */
    @Deprecated
    protected void dragAndDropBy(WebElement source, int xOffset, int yOffset) {
        checkNotNull(source, "Source WebElement can not be null");
        new Actions(getDriver()).dragAndDropBy(source, xOffset, yOffset).perform();
    }

    /**
     * @deprecated (when, can directly use action is calling class, keeping for reference...)
     */
    @Deprecated
    protected void rightClick(WebElement webElement) {
        checkNotNull(webElement, "WebElement can not be null");
        action("Right click", webElement, pe -> new Actions(getDriver()).moveToElement(pe).contextClick(pe).perform());
    }

    protected void scrollIntoView(WebElement webElement) {
        checkNotNull(webElement, "WebElement can not be null");
        action(webElement, pe -> ((JavascriptExecutor) getDriver()).executeScript("argument[0].scrollIntoView(true);", pe));
    }

    protected void scrollDown() {
        ((JavascriptExecutor) getDriver()).executeScript("window.scrollBy(0,250)");
    }

    protected void scrollUp() {
        ((JavascriptExecutor) getDriver()).executeScript("window.scrollBy(250,0)");
    }

    protected void scrollByCordinates(WebElement webElement) {
        checkNotNull(webElement, "WebElement can not be null");
        ((Locatable) webElement).getCoordinates().inViewPort();
    }

    protected boolean brokenImage(WebElement webElement) {
        checkNotNull(webElement, "WebElement can not be null");
        EventFiringWebDriver eventFiringWebDriver = new EventFiringWebDriver(getDriver());
        String script = "return (typeof arguments[0].naturalWidth !='undefined' && arguments[0].naturalWidth>0)";
        return (boolean) eventFiringWebDriver.executeScript(script, webElement);
    }

    protected String getPageSource() {
        return getDriver().getPageSource();
    }

    protected void enterText(String text, WebElement webElement) {
        checkArgument(StringUtils.isNotBlank(text), "text can not be empty or blank");
        checkNotNull(webElement, "WebElement can not be null");
        action(webElement, WebElement::clear);
        action(String.format("clear and enter text '%s'", text), webElement, we -> we.sendKeys(text));
    }

    protected void jsEnterText(String text, WebElement webElement) {
        checkArgument(StringUtils.isNotBlank(text), "text can not be empty or blank");
        checkNotNull(webElement, "WebElement can not be null");
        action("click", webElement, we -> ((JavascriptExecutor) this.getDriver()).executeScript("arguments[0].value='" + text + "';", we));
    }

    protected void jsClick(WebElement webElement) {
        checkArgument(nonNull(webElement), "WebElement can not be null");
        action("click", webElement, we -> ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click()", we));
    }

    protected void click(WebElement webElement) {
        checkNotNull(webElement, "WebElement can not be null");
        try {
            safeClick(webElement);
        } catch (TimeoutException te) {
            logWarning("Could not click element, attempt to move to element and click");
            moveToElement(webElement);
            try {
                safeClick(webElement);
            } catch (TimeoutException ex) {
                logWarning("Could not click element, attempt to scroll to view to element and click");
                scrollIntoView(webElement);
                safeClick(webElement);
            }
        }
    }
}
