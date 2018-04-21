package com.automation.pages;

import com.automation.annotations.PageUrl;
import com.automation.framework.AbstractPage;
import com.automation.framework.WebUser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.automation.reports.ExtentTestManager.LogInfo;

@PageUrl(url = "/")
public class HomePage extends AbstractPage {

    private final HomePageValidator validator = new HomePageValidator(this);
    @FindBy(css = "div[class='tbar-top hidden-sm hidden-xs'] li[id='li_myaccount']>a[class='dropdown-toggle go-text-right']")
    private WebElement myAccountLink;
    @FindBy(css = "div[class='tbar-top hidden-sm hidden-xs'] li[id='li_myaccount']>ul[class='dropdown-menu'] li>a[href='https://www.phptravels.net/login']")
    private WebElement loginLink;

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
        return (LoginPage) loadPage(LoginPage.class);
    }

    public HomePageValidator verify() {
        return validator;
    }
}


