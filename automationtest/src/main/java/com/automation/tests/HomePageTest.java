package com.automation.tests;

import com.automation.CustomContext;
import com.automation.framework.BaseTest;
import com.automation.framework.DataProviderClass;
import com.automation.pages.FlightsPage;
import com.automation.pages.HomePage;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

public class HomePageTest extends BaseTest {

    @Factory(dataProvider = "testDataProvide", dataProviderClass = DataProviderClass.class)
    HomePageTest(CustomContext customContext) {
        super(customContext);
    }

    @Test
    public void testHomePage() {
        List propertyType = new LinkedList();
        propertyType.add("Hotel");
        propertyType.add("Motel");

        web()
                .openPageUrl(HomePage.class)
                .enterCheckInDate("25/09/2018")
                .enterCheckOutDate("25/09/2018")
                .clickSearch()
                .verify().checkMessage("No Results Found")
                .selectRating(3)
                .selectPropertyType(propertyType);
    }

    @Test
    public void testFlightsPage(){
        web()
                .openPageUrl(FlightsPage.class)
                .enterDepartureDate("25/09/2018")
                .selectCabinClass("first")
                .clickSearch();
    }

//    @Test
//    public void testLoginPage() {
//        List propertyType = new LinkedList();
//        propertyType.add("Hotel");
//        propertyType.add("Motel");
//
//        web()
//                .openPageUrl(HomePage.class)
//                .enterCheckInDate("25/09/2018")
//                .enterCheckOutDate("25/09/2018")
//                .clickSearch()
//                .verify().checkMessage("No Results Found")
//                .selectRating(3)
//                .selectPropertyType(propertyType);
//    }

    @Test
    public void testLoginWithCredentials() {
        web()
                .openPageUrl(HomePage.class)
                .clickLogin()
                .enterCredentials("user@phptravels.com", "demouser");
    }

    @Test
    public void testHomePageTitle() {
        web()
                .openPageUrl(HomePage.class)
                .verify().isTitleDisplayed();

    }

    @Test
    public void endToEndFlow(){
        web()
                .openPageUrl(HomePage.class)

                .verify().isTitleDisplayed();
    }

}
