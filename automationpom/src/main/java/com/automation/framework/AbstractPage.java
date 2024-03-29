package com.automation.framework;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Locatable;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.automation.reports.ExtentTestManager.logInfo;
import static com.automation.reports.ExtentTestManager.logWarning;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class AbstractPage {

    private static final Set<Class<? extends Throwable>> IGNORE_EXCEPTIONS_WHILE_WAITING_SET = ImmutableSet.of(NotFoundException.class, ElementNotVisibleException.class, IndexOutOfBoundsException.class, NullPointerException.class, StaleElementReferenceException.class, IllegalStateException.class, new Class[]{NoSuchFrameException.class, WebDriverException.class});
    private static final String LOG_MESSAGE_CAN_NOT_BE_NULL_OR_EMPTY = "log message can not be null or empty";
    public static final Map<String, Map<String, PageObject>> objectMap = new HashMap<String, Map<String, PageObject>>();
    private String pageName;
	private static final Integer DEFAULT_POLLING = 2;
	private static final Integer DEFAULT_TIMEOUT = 2;

	@Getter
	private final WebUser webUser;

    protected AbstractPage(WebUser webUser) {
        this.webUser = webUser;
        PageFactory.initElements(this.webUser.getDriver(), this);
        pageName = this.getClass().getSimpleName();
        if (!objectMap.containsKey(pageName)) {
            String mapFileName = this.getClass().getPackage().getName() + "." + pageName;
            loadObjectRepository(mapFileName);
        }
    }

    private WebDriver getDriver() {
        return webUser.getDriver();
    }

    private void safeClick(WebElement webElement) {
        action("Click", webElement, WebElement::click, this);
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
        logInfo(logMessage);
        return action(element, action, page);
    }

    private <T extends AbstractPage> T action(WebElement webElement, Consumer<WebElement> action, T page) {
        action(webElement, action);
        return page;
    }

    protected AbstractPage action(String logMessage, WebElement webElement, Consumer<WebElement> action) {
        checkArgument(isNotBlank(logMessage), LOG_MESSAGE_CAN_NOT_BE_NULL_OR_EMPTY);
        logInfo(logMessage);
        return action(webElement, action);
    }

    private AbstractPage action(WebElement webElement, Consumer<WebElement> action) {
        checkNotNull(webElement, "WebElement can not be null");
        action.accept(webElement);
        return this;
    }

    protected <T extends AbstractPage> T action(String logMessage, Select element, Consumer<Select> action, T page) {
        action(logMessage, element, action);
        return page;
    }

    private AbstractPage action(String logMessage, Select element, Consumer<Select> action) {
        logInfo(logMessage);
        return action(element, action);
    }

    protected AbstractPage action(Select element, Consumer<Select> action) {
        checkNotNull(element, "Select can not be null");
        action.accept(element);
        return this;
    }

    protected AbstractPage wait(String logMessage, WebElement element, int timeout) {
        return wait(logMessage, element, pe -> pe.isEnabled(), timeout);
    }

    protected AbstractPage wait(String logMessage, WebElement webElement, Function<WebElement, Boolean> condition, int timeout) {
        checkArgument(isNotBlank(logMessage), LOG_MESSAGE_CAN_NOT_BE_NULL_OR_EMPTY);
        checkNotNull(webElement, "WebElement can not be null");
        logInfo(logMessage);

		new FluentWait<>(webElement)
				.withTimeout(Duration.ofSeconds(timeout))
				.pollingEvery(Duration.ofSeconds(DEFAULT_POLLING))
				.ignoreAll(IGNORE_EXCEPTIONS_WHILE_WAITING_SET)
				.until(condition);
		return this;
	}

	protected WebElement wait(String logMessage, ExpectedCondition<WebElement> condition, int timeout) {
		checkArgument(isNotBlank(logMessage), LOG_MESSAGE_CAN_NOT_BE_NULL_OR_EMPTY);
		logInfo(logMessage);
		return new FluentWait<>(getDriver())
				.withTimeout(Duration.ofSeconds(timeout))
				.pollingEvery(Duration.ofSeconds(DEFAULT_POLLING))
				.ignoreAll(IGNORE_EXCEPTIONS_WHILE_WAITING_SET)
				.until(condition);
	}

    protected AbstractPage moveToElement(WebElement webElement) {
        return action("Moving to Element", webElement, we -> new Actions(getDriver()).moveToElement(we).perform(), this);
    }

	protected Alert switchToAlert() {
		return switchToAlert(DEFAULT_TIMEOUT);
	}

	protected Alert switchToAlert(int timeout) {
		return new FluentWait<>(getDriver())
				.withTimeout(Duration.ofSeconds(timeout))
				.pollingEvery(Duration.ofSeconds(DEFAULT_POLLING))
				.ignoreAll(IGNORE_EXCEPTIONS_WHILE_WAITING_SET)
				.until(driver -> driver.switchTo().alert());
	}

    protected AbstractPage switchToWindow(String windowName) {
        return switchToWindow(windowName, DEFAULT_TIMEOUT);
    }

    protected AbstractPage switchToWindow(String windowName, int timeout) {
        checkArgument(StringUtils.isNotBlank(windowName), "Window name can not be null or blank");
        new FluentWait<>(getDriver())
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(DEFAULT_POLLING))
                .ignoreAll(IGNORE_EXCEPTIONS_WHILE_WAITING_SET)
                .until(driver -> driver.switchTo().window(windowName));
        return this;
    }

    protected AbstractPage switchToiFrame(String frameName) {
        return switchToiFrame(frameName, DEFAULT_TIMEOUT);
    }

    protected AbstractPage switchToiFrame(String frameName, int timeout) {
        checkArgument(StringUtils.isNotBlank(frameName), "Frame name can not be null or blank");
        new FluentWait<>(getDriver())
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(DEFAULT_POLLING))
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
        } catch (Exception te) {
            try {
                logWarning("Could not click element, attempt to move to element and click");
                wait("Wail till element displayed ", webElement, DEFAULT_TIMEOUT);
                safeClick(webElement);
            } catch (Exception te1) {
                logWarning("Could not click element, attempt to move to element and click");
                moveToElement(webElement);
                try {
                    safeClick(webElement);
                } catch (Exception ex) {
                    logWarning("Could not click element, attempt to scroll to view to element and click");
                    scrollIntoView(webElement);
                    safeClick(webElement);
                }
            }
        }
    }

    private void loadObjectRepository(String Resources_path) {
        ResourceBundle rb;
        try {
            rb = ResourceBundle.getBundle(Resources_path);
            Enumeration<String> keys = rb.getKeys();
            Map<String, PageObject> pageObjects = new HashMap<String, PageObject>();
            ;
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                String value = rb.getString(key);
                String arr[] = value.split(";");
                pageObjects.put(key, new PageObject(arr[0], arr[1]));
            }
            objectMap.put(pageName, pageObjects);
        } catch (Exception e) {
            System.out.println("Err occurred" + e.toString());
        }
    }

    protected WebElement getWebElement(String element){
        if(objectMap.containsKey(pageName)){
            PageObject object = objectMap.get(pageName).get(element);
            if(object != null){
                return getDriver().findElement(getBy(object.Identifier,object.FindBy));
            }
        }
        return null;
    }

    protected By getBy(String elementIdentifier, String findBy) {
        switch (findBy.toLowerCase()) {
            case "name":
                return By.name(elementIdentifier);
            case "id":
                return By.id(elementIdentifier);
            case "xpath":
                return By.xpath(elementIdentifier);
            case "linktext":
                return By.linkText(elementIdentifier);
            case "css":
                return By.cssSelector(elementIdentifier);
            case "tagname":
                return By.tagName(elementIdentifier);
            case "classname":
                return By.className(elementIdentifier);
        }
        return null;
    }

    public class PageObject {
        public String Identifier;
        public String FindBy;

        public PageObject(String identifier, String findBy) {
            Identifier = identifier;
            FindBy = findBy;
        }
    }
}
