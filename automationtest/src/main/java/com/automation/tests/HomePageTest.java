package com.automation.tests;

import java.util.LinkedList;
import java.util.List;

import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.automation.CustomContext;
import com.automation.framework.BaseTest;
import com.automation.framework.DataProviderClass;
import com.automation.pages.flight.FlightsPage;
import com.automation.pages.home.HomePage;

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
				.enterCheckInDate("25/09/2019")
				.enterCheckOutDate("25/09/2019")
				.clickSearch()
//                .verify().checkMessage("No Results Found")
				.selectRating(3)
				.selectPropertyType(propertyType);

		web()
				.jsNewWindow("https://www.google.com");
//        api()
//                .getTradeValidationApi()

	}

	@Test
	public void testFlightsPage() {
		web()
				.openPageUrl(FlightsPage.class)
				.verify().checkPageUrl()
				.enterDepartureDate("25/09/2019")
				.selectCabinClass("first")
				.clickSearch();
	}

	@Test
	public void testLoginPage() {
		List propertyType = new LinkedList();
		propertyType.add("Hotel");
		propertyType.add("Motel");

		web()
				.openPageUrl(HomePage.class)
				.enterCheckInDate("25/09/2019")
				.enterCheckOutDate("25/09/2019")
				.clickSearch()
				.selectRating(3)
				.selectPropertyType(propertyType);
	}

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
	public void endToEndFlow() {
		web()
				.openPageUrl(HomePage.class)

				.verify().isTitleDisplayed();
	}
}
