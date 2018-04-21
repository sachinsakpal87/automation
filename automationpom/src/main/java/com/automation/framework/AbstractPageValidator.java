package com.automation.framework;

public abstract class AbstractPageValidator<T extends AbstractPage> {
    private final T page;

    protected AbstractPageValidator(T page) {
        this.page = page;
    }

    protected T getPage() {
        return page;
    }

}
