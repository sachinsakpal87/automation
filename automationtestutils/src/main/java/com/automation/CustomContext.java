package com.automation;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.util.HashMap;
import java.util.Objects;

import lombok.Getter;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;

@Getter
public final class CustomContext {
	private static final String THIS_PTR_CUSTOM_CONTEXT = "THIS_PTR_CUSTOM_CONTEXT";
	private final ITestContext itestContext;
	private final TestNGParameters testNGParameters;
	private final HashMap<String, Object> savedData = new HashMap<>();
	private WebDriver webDriver;

	private CustomContext(ITestContext iTestContext) {
		System.out.println("ITest Context" + iTestContext.getName() + "->" + iTestContext.toString());
		this.itestContext = iTestContext;
		testNGParameters = new TestNGParameters(iTestContext);
		this.itestContext.setAttribute(THIS_PTR_CUSTOM_CONTEXT, this);
	}

	/**
	 * Build new custom context.
	 *
	 * @param iTestContext iTestContext.
	 * @return Object of custom context.
	 */
	public static CustomContext buildCustomContext(ITestContext iTestContext) {
		checkArgument(Objects.nonNull(iTestContext), "ITestContext value is null");
		System.out.println("Build Custom context called");
		return new CustomContext(iTestContext);
	}

	/**
	 * Save data to custom context.
	 *
	 * @param key   String key name.
	 * @param value Object value.
	 */
	public void saveDataToContext(String key, Object value) {
		checkArgument(StringUtils.isNotBlank(key), "value should not be null or empty");
		checkArgument(Objects.nonNull(value), "value should not be null");
		savedData.put(key, value);
	}

	/**
	 * Read data from custom context
	 *
	 * @param key String key name.
	 * @return Return Object value for a given key.
	 */
	public Object getDataFromContext(String key) {
		checkArgument(StringUtils.isNotBlank(key), "Key should not be null or empty");
		return savedData.get(key);
	}

	/**
	 * Get webdriver object from thread local.
	 *
	 * @return webdriver object.
	 */
	private WebDriver getDriver() {
		WebDriver driver = webDriver;
		checkState(Objects.nonNull(driver), "Driver is not set in customcontext.");
		return driver;
	}

	/**
	 * Set webdriver object in thread local.
	 *
	 * @param driver webdriver object.
	 */
	public void setDriver(WebDriver driver) {
		webDriver = driver;
	}

	public ITestContext getTestNGContext() {
		return itestContext;
	}
}
