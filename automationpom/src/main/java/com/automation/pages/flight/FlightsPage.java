package com.automation.pages.flight;

import static com.google.common.base.Preconditions.checkArgument;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import com.automation.annotations.PageUrl;
import com.automation.framework.AbstractPage;
import com.automation.framework.WebUser;

@PageUrl(url = "/flights")
public class FlightsPage extends AbstractPage {

	private final FlightsPageValidator validator = new FlightsPageValidator(this);

	@FindBy(css = "input[name='departure']")
	private WebElement departure;
	@FindBy(css = "input[name='arrival']")
	private WebElement arrival;
	@FindBy(css = "select[name='cabinclass']")
	private WebElement cabinClass;
	@FindBy(xpath = "//form[@name='flightmanualSearch']//button[contains(text(),'Search')]")
	private WebElement search;

	public FlightsPage(WebUser user) {
		super(user);
	}

	public FlightsPageValidator verify() {
		return validator;
	}

	public FlightsPage enterDepartureDate(String date) {
		checkArgument(StringUtils.isNotBlank(date), "date should not be null or empty");
		action("msg", departure, pe -> pe.isDisplayed());
		return action(String.format("Entering departure date as %s", date), departure, pe -> pe.sendKeys(date), this);
	}

	public FlightsPage enterArrivalDate(String date) {
		checkArgument(StringUtils.isNotBlank(date), "date should not be null or empty");
		return action(String.format("Entering arrival date as %s", date), arrival, pe -> pe.sendKeys(date), this);
	}

	public FlightsPage selectCabinClass(String cabinclass) {
		checkArgument(StringUtils.isNotBlank(cabinclass), "Cabin class should not be null or empty");
		return action(String.format("Select Cabin class %s", cabinclass), new Select(cabinClass), pe -> pe.selectByValue(cabinclass), this);
	}

	public FlightsPage clickSearch() {
		return action("Clicking search", search, this::click, this);
	}
}


