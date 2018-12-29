package com.automation.pages;

import com.automation.annotations.PageUrl;
import com.automation.framework.AbstractPage;
import com.automation.framework.WebUser;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.google.common.base.Preconditions.checkArgument;

@PageUrl(url = "/login")
public class LoginPage extends AbstractPage {

    @FindBy(css = "input[name='username']")
    private WebElement username;

    @FindBy(css = "input[name='password']")
    private WebElement password;

    @FindBy(css = "button.loginbtn")
    private WebElement login;

    public LoginPage(WebUser webUser) {
        super(webUser);
    }

    public LoginPage enterCredentials(String username, String password) {
        checkArgument(StringUtils.isNotBlank(username), "Username should not be null or empty");
        checkArgument(StringUtils.isNotBlank(password), "password should not be null or empty");
        wait("Wait till username displayed", this.username,pe->pe.isDisplayed(), 4);
        if (this.username.isDisplayed()) {
            action("Clearing Username :: ", this.username, WebElement::clear);
            action(String.format("Entering Username :: %s", username), this.username, we -> we.sendKeys(username));
            action("Clearing password :: ", this.username, WebElement::clear);
            action(String.format("Entering password :: %s", password), this.password, we -> we.sendKeys(password));
            action("Clicking Login", login, this::click);
        } else {
            throw new ArithmeticException("");
        }
        return this;
    }

}
