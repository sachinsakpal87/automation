package com.automation.pages;

import com.automation.framework.AbstractPageValidator;
import org.hamcrest.Matchers;

import static org.hamcrest.MatcherAssert.assertThat;

public class HomePageValidator extends AbstractPageValidator {

    HomePageValidator(HomePage page) {
        super(page);
    }

    @Override
    protected HomePage getPage() {
        return (HomePage) super.getPage();
    }

    public HomePage isTitleDisplayed() {
        assertThat(!getPage().getPageTitle().isEmpty(), Matchers.is(true));
        return getPage();
    }
}
