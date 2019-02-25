package com.automation.framework;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import com.automation.CustomContext;

public class DataProviderClass {

	private DataProviderClass() {
		throw new IllegalStateException("Utility class");
	}

	@DataProvider(name = "testDataProvide")
	public static Object[][] testDataProviderClass(ITestContext iTestContext) {
		return new Object[][] {
				new Object[] { CustomContext.buildCustomContext(iTestContext) }
		};
	}
}
