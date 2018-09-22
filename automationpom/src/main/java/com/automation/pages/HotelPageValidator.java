package com.automation.pages;

import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class HotelPageValidator extends HomePageValidator {

    HotelPageValidator(HotelPage page) {
        super(page);
    }

    @Override
    protected HotelPage getPage() {
        return (HotelPage) super.getPage();
    }

    @Override
    public HotelPage isTitleDisplayed() {
        assertThat(!getPage().getPageTitle().isEmpty(), is(true));
        return getPage();
    }

    public HotelPage checkMessage(String message){
        checkArgument(StringUtils.isNotBlank(message),"Message should not be null or empty");
        assertThat("Incorrect error message",message,is(equalTo(getPage().message.getText())));
        return getPage();
    }

}
