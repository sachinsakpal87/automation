package com.automation.pages.home;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matchers;

import com.automation.framework.AbstractPageValidator;

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
