package com.automation.core;

import static com.automation.reports.ExtentTestManager.logFail;
import static com.automation.reports.ExtentTestManager.logInfo;
import static com.automation.reports.ExtentTestManager.logPass;
import static com.google.common.base.Preconditions.checkArgument;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.nullValue;

import java.util.Arrays;
import java.util.List;

import com.automation.application.apis.ToolsQAWeatherApi;
import com.automation.environment.EnvironmentProperties;

public class ApiWrapper extends RestAssuredWrapper {
	private final EnvironmentProperties environmentProperties;

	public ApiWrapper(EnvironmentProperties environmentProperties) {
		this.environmentProperties = environmentProperties;
	}

	public ToolsQAWeatherApi getTradeValidationApi() {
		logInfo("Creating API client for ToolsQAWeatherApi");
		return new ToolsQAWeatherApi(this, environmentProperties);
	}

	public ApiWrapper verifyResponseCode(Integer code) {
		checkArgument(code != null, "Expected response code cannot be null");
		int value = getResponse().getStatusCode();
		if (equalTo(code).matches(value)) {
			logPass("Verified that response code is: %s", code.toString());
		} else {
			AssertionError assertionError = new AssertionError("Response code assertion failed");
			logFail("Response code was not: %s", assertionError, code.toString());
			throw assertionError;
		}
		return this;
	}

	public ApiWrapper verifyResponseAttribute(String attributePath, String expectedValue) {
		String value = getResponseAttribute(attributePath).toString();

		if (equalTo(expectedValue).matches(value)) {
			logPass("Verified that required response attribute: %s value is : %s", attributePath, expectedValue);
		} else {
			logFail(String.format("Failed, required attribute:%s value was %s instead of %s", attributePath, value, expectedValue), null);
		}
		return this;
	}

	public ApiWrapper verifyResponseAttributeListValue(String attributePath, String[] expectedValues) {
		List<String> value = getResponseAttributeAsList(attributePath, false);
		Arrays.asList(expectedValues).forEach(s -> {
			if (isIn(value).matches(s)) {
				logPass("Verified that required response attribute: %s value contains : %s", attributePath, s);
			} else {
				logFail(String.format("Failed, required attribute:%s value not contains %s", attributePath, s), null);
			}
		});
		return this;
	}

	public ApiWrapper verifyResponseAttributeAsListAsEmpty(String attributePath) {
		List<String> value = getResponseAttributeAsList(attributePath, true);

		if (is(nullValue()).matches(value)) {
			logPass("Verified that required response attribute: %s value is null or empty", attributePath);
		} else {
			logFail(String.format("Failed, required attribute:%s value is not null or empty", attributePath), null);
		}
		return this;
	}
}