package com.automation.framework;

import com.automation.annotations.PageUrl;
import com.gargoylesoftware.htmlunit.AbstractPage;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;


class PageUrls<T extends AbstractPage> {


    public static String getUrl(Class clazz) {
        PageUrl pageUrl = getPageUrlAnnotation(clazz);
        Preconditions.checkArgument(Objects.nonNull(pageUrl) && StringUtils.isNotBlank(pageUrl.url()), "Url is not set for class " + clazz.getName());
        return pageUrl.url();
    }

    private static PageUrl getPageUrlAnnotation(Class clazz) {
        if (!clazz.isAnnotationPresent(PageUrl.class))
            return null;
        else {
            return (PageUrl) clazz.getDeclaredAnnotation(PageUrl.class);
        }
    }
}
