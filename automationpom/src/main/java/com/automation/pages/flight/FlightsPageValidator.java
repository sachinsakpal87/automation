package com.automation.pages.flight;

import com.automation.framework.AbstractPageValidator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FlightsPageValidator extends AbstractPageValidator<FlightsPage> {

    FlightsPageValidator(FlightsPage page) {
        super(page);
    }

    @Override
    public FlightsPage getPage() {
        return super.getPage();
    }

    public FlightsPage isTitleDisplayed() {
        assertThat(!getPage().getPageTitle().isEmpty(), is(true));
        return getPage();
    }
}
