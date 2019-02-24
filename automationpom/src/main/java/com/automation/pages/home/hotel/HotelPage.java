package com.automation.pages.home.hotel;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.automation.annotations.PageUrl;
import com.automation.framework.WebUser;
import com.automation.pages.home.HomePage;

@PageUrl(url = "/hotels")
public class HotelPage extends HomePage {

	private final HotelPageValidator validator = new HotelPageValidator(this);
	@FindBy(xpath = "//div[@class='listingbg']/div[@class='container']//h2")
	protected WebElement message;

	public HotelPage(WebUser user) {
		super(user);
	}

	@Override
	public HotelPageValidator verify() {
		return validator;
	}
}


