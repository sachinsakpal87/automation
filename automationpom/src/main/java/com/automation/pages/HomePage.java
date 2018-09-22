package com.automation.pages;

import com.automation.annotations.PageUrl;
import com.automation.framework.AbstractPage;
import com.automation.framework.WebUser;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static com.automation.reports.ExtentTestManager.LogInfo;
import static com.google.common.base.Preconditions.checkArgument;

@PageUrl(url = "/")
public class HomePage extends AbstractPage {

    private final HomePageValidator validator = new HomePageValidator(this);
    @FindBy(css = "nav.navbar-default li[id='li_myaccount']>a")
    private WebElement myAccountLink;
    @FindBy(css = "div[class='tbar-top hidden-sm hidden-xs'] li[id='li_myaccount']>ul[class='dropdown-menu'] li>a[href='https://www.phptravels.net/login']")
    private WebElement loginLink;
    @FindBy(css = "input[name='checkin']")
    private WebElement checkIn;
    @FindBy(css = "input[name='checkout']")
    private WebElement checkOut;
    @FindBy(xpath = "//form[@name='fCustomHotelSearch']//button[contains(text(),'Search')]")
    private WebElement search;
    @FindBy(css = "div.rating")
    private WebElement ratingsList;

    public HomePage(WebUser user) {
        super(user);
    }

    private HomePage clickMyAccount() {
        LogInfo("Clicking my account");
        myAccountLink.click();
        return this;
    }

    public LoginPage clickLogin() {
        LogInfo("Clicking login");
        clickMyAccount();
        loginLink.click();
        return loadPage(LoginPage.class);
    }

    public HomePageValidator verify() {
        return validator;
    }

    public HomePage enterCheckInDate(String date) {
        checkArgument(StringUtils.isNotBlank(date), "date should not be null or empty");
        action(String.format("Entering check-in Date as %s",date), checkIn, pe -> pe.sendKeys(date),this);
        return this;
    }

    public HomePage enterCheckOutDate(String date) {
        checkArgument(StringUtils.isNotBlank(date), "date should not be null or empty");
        action(String.format("Entering check-out Date as %s",date), checkOut, pe -> pe.sendKeys(date),this);
        return this;
    }

    public HotelPage clickSearch(){
        return action( "Clicking search",search, this::click,loadPage(HotelPage.class));
    }

    public HomePage selectRating(int rating){
        action("Selecting rating", ratingsList.findElement(By.xpath(String.format("//input[@id='%d']/following-sibling::ins",rating))),this::click,this);
        return this;
    }

    public HomePage selectPropertyType(List<String> list){
        list.forEach(li->action(String.format("Selecting Property type %s",li),findElement(By.xpath(String.format("//input[@type='checkbox' and @id='%s']/following-sibling::ins",li))), this::click,this));
        return this;
    }
}

