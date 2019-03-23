package com.automation.pages.home;

import com.automation.framework.AbstractPageValidator;
import org.hamcrest.Matchers;

import static org.hamcrest.MatcherAssert.assertThat;

public class HomePageValidator extends AbstractPageValidator {

    protected HomePageValidator(HomePage page) {
        super(page);
    }

    @Override
    public HomePage getPage() {
        return (HomePage) super.getPage();
    }

    public HomePage isTitleDisplayed() {
        assertThat(!getPage().getPageTitle().isEmpty(), Matchers.is(true));
        return getPage();
    }
}
