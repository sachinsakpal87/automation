package com.automation.framework;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Locatable;
import org.openqa.selenium.support.PageFactory;
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

import static com.automation.reports.ExtentTestManager.LogInfo;
import static com.automation.reports.ExtentTestManager.LogWarning;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class AbstractPage {

    private static final Set<Class<? extends Throwable>> IGNORE_EXCEPTIONS_WHILE_WAITING_SET = ImmutableSet.of(NotFoundException.class, ElementNotVisibleException.class, IndexOutOfBoundsException.class, NullPointerException.class, StaleElementReferenceException.class, IllegalStateException.class, new Class[]{NoSuchFrameException.class, WebDriverException.class});

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
        wait("Wail till element displayed ", webElement, 5);
        action("Click", webElement, arg -> arg.click(), this);
    }

    public String getPageTitle() {
        return getDriver().getTitle();
    }

    protected String getCurrentUrl() {
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
        checkArgument(isNotBlank(logMessage), "log message can not be null or empty");
        LogInfo(logMessage);
        return action(element, action, page);
    }

    protected <T extends AbstractPage> T action(WebElement element, Consumer<WebElement> action, T page) {
        checkArgument(nonNull(element), "WebElement can not be null");
        action(element, action);
        return page;
    }

    protected AbstractPage action(String logMessage, WebElement element, Consumer<WebElement> action) {
        checkArgument(isNotBlank(logMessage), "log message can not be null or empty");
        LogInfo(logMessage);
        return action(element, action);
    }

    protected AbstractPage action(WebElement element, Consumer<WebElement> action) {
        checkArgument(nonNull(element), "WebElement can not be null");
        action.accept(element);
        return this;
    }

    protected <T extends AbstractPage> T action(String logMessage, Select element, Consumer<Select> action, T page) {
        action(logMessage,element,action);
        return page;
    }

    protected AbstractPage action(String logMessage, Select element, Consumer<Select> action) {
        checkArgument(isNotBlank(logMessage), "log message can not be null or empty");
        checkArgument(nonNull(element), "Select can not be null");
        LogInfo(logMessage);
        return action(element, action);
    }

    protected AbstractPage action(Select element, Consumer<Select> action) {
        checkArgument(nonNull(element), "WebElement can not be null");
        action.accept(element);
        return this;
    }

    protected WebElement wait(String logMessage, WebElement element, int timeout) {
        return wait(logMessage, ExpectedConditions.visibilityOf(element), 5);
    }

    protected WebElement wait(String logMessage, WebElement element, ExpectedCondition<WebElement> condition, int timeout) {
        return wait(logMessage, condition, 5);
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

    protected WebElement wait(String logMessage, ExpectedCondition<WebElement> condition, int timeout) {
        checkArgument(isNotBlank(logMessage), "log message can not be null or empty");
        LogInfo(logMessage);
        return new FluentWait<>(getDriver())
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoreAll(IGNORE_EXCEPTIONS_WHILE_WAITING_SET)
                .until(condition);
    }

    protected AbstractPage moveToElement(WebElement webElement) {
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
        return switchToWindow(windowName, 5);
    }

    protected AbstractPage switchToWindow(String windowName, int timeout) {
        new FluentWait<>(getDriver())
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoreAll(IGNORE_EXCEPTIONS_WHILE_WAITING_SET)
                .until(driver -> driver.switchTo().window(windowName));
        return this;
    }

    protected AbstractPage switchToiFrame(String frameName) {
        return switchToiFrame(frameName, 5);
    }

    protected AbstractPage switchToiFrame(String frameName, int timeout) {
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
        action("Drag and Drop", source, we -> new Actions(getDriver()).dragAndDrop(source, destination).perform());
    }

    @Deprecated
    protected void dragAndDrop(WebElement source, WebElement destination, boolean old) {
        action("click and hold", source, we -> new Actions(getDriver()).clickAndHold(source));
        moveToElement(destination);
        action("release", source, we -> new Actions(getDriver()).release());
    }

    @Deprecated
    protected void dragAndDropBy(WebElement source, int xOffset, int yOffset) {
        new Actions(getDriver()).dragAndDropBy(source, xOffset, yOffset).perform();
    }

    /**
     * Can directly use action method in page class as used in this method.
     */
    @Deprecated
    protected void rightClick(WebElement element) {
        action("Right click", element, pe -> new Actions(getDriver()).moveToElement(pe).contextClick(pe).perform());
//        new Actions(getDriver()).moveToElement(element).contextClick(element).build().perform();
    }

    protected void scrollIntoView(WebElement element){
        action(element,pe->((JavascriptExecutor)getDriver()).executeScript("argument[0].scrollIntoView(true);",pe));
    }

    protected void scrollDown(){
        ((JavascriptExecutor)getDriver()).executeScript("window.scrollBy(0,250)","");
    }

    protected void scrollUp(){
        ((JavascriptExecutor)getDriver()).executeScript("window.scrollBy(250,0)","");
    }

    protected void scrollByCordinates(WebElement element){
        ((Locatable)element).getCoordinates().inViewPort();
    }

    protected void click(WebElement webElement) {
        try {
            safeClick(webElement);
        } catch (TimeoutException te) {
            LogWarning("Could not click element, attempt to move to element and click");
            moveToElement(webElement);
            safeClick(webElement);
        }
    }
}

//        protected AbstractPage wait(String logMessage, WebElement element, int timeout) {
//        return wait(logMessage, element, WebElement::isDisplayed, timeout);
//    }
//
//    protected AbstractPage wait(String logMessage, WebElement element, Function<WebDriver, Boolean> condition, int timeout) {
//        checkArgument(isNotBlank(logMessage), "log message can not be null or empty");
//        checkArgument(nonNull(element), "WebElement can not be null");
//        LogInfo(logMessage);
//
//        new FluentWait<>(getDriver())
//                .withTimeout(Duration.ofSeconds(timeout))
//                .pollingEvery(Duration.ofSeconds(5))
//                .ignoreAll(IGNORE_EXCEPTIONS_WHILE_WAITING_SET)
//                .until(condition);
//        return this;
//    }

//    protected AbstractPage webDriverWait(WebElement element){
//        WebDriverWait wait = new WebDriverWait(getDriver(),10);
//        wait.until(ExpectedConditions.visibilityOf(element));
//
//    }
