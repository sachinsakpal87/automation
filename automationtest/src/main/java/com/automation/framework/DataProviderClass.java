package com.automation.framework;

import com.automation.CustomContext;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

public class DataProviderClass {

    @DataProvider(name = "testDataProvide")
    public static Object[][] testDataProviderClass(ITestContext iTestContext) {
        return new Object[][]{
                new Object[]{CustomContext.buildCustomContext(iTestContext)}
        };
    }
}
