package com.automation.tests;

import com.automation.CustomContext;
import com.automation.framework.BaseTest;
import com.automation.framework.DataProviderClass;
import com.automation.pages.HomePage;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class HomePageTest extends BaseTest {

    @Factory(dataProvider = "testDataProvide", dataProviderClass = DataProviderClass.class)
    HomePageTest(CustomContext customContext) {
        super(customContext);
    }

//    @Test
//    public void testHomePage() {
//        web()
//                .openPageUrl(HomePage.class);
//        throw new ArithmeticException("Exception occurred");
//    }
//
//    @Test
//    public void testLoginPage() {
//        web()
//                .openPageUrl(HomePage.class)
//                .clickLogin();
//    }
//
//    @Test
//    public void testLoginWithCredentials() {
//        web()
//                .openPageUrl(HomePage.class)
//                .clickLogin()
//                .enterCredentials("user@phptravels.com", "demouser");
//    }
//
//    @Test
//    public void testHomePageTitle() {
//        web()
//                .openPageUrl(HomePage.class)
//                .verify().isTitleDisplayed();
//
//    }

    @Test
    public void endToEndFlow(){
        web()
                .openPageUrl(HomePage.class)
                .verify().isTitleDisplayed();
    }

}
