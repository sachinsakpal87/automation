package com.automation.framework;

import static com.automation.reports.ExtentTestManager.logInfo;
import static org.hamcrest.EasyMock2Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matchers;

public abstract class AbstractPageValidator<T extends AbstractPage> {
    private final T page;

    public AbstractPageValidator(T page) {
        this.page = page;
    }

    public T getPage() {
        return page;
    }

    public T checkPageUrl(){
        logInfo("Verifying URL");
        assertThat("Failed because of URL mismatch ",getPage().getBrowserUrl(), Matchers.containsString(PageUrls.getUrl(page.getClass())));
        return page;
    }

}
