package com.automation.pages;

import com.automation.annotations.PageUrl;
import com.automation.framework.WebUser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@PageUrl(url = "/hotels")
public class HotelPage extends HomePage {

    @FindBy(xpath = "//div[@class='listingbg']/div[@class='container']//h2")
    protected WebElement message;

    private final HotelPageValidator validator = new HotelPageValidator(this);

    public HotelPage(WebUser user) {
        super(user);
    }

    public HotelPageValidator verify() {
        return validator;
    }
}


