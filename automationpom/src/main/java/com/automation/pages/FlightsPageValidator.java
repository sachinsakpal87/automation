package com.automation.pages;

import com.automation.framework.AbstractPageValidator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FlightsPageValidator extends AbstractPageValidator {

    FlightsPageValidator(FlightsPage page) {
        super(page);
    }

    @Override
    protected FlightsPage getPage() {
        return (FlightsPage) super.getPage();
    }

    public FlightsPage isTitleDisplayed() {
        assertThat(!getPage().getPageTitle().isEmpty(), is(true));
        return getPage();
    }
}
