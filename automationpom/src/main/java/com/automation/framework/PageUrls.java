package com.automation.framework;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.automation.annotations.PageUrl;
import com.google.common.base.Preconditions;

class PageUrls {

	private PageUrls() {
		throw new IllegalStateException("Utility class");
	}

	public static String getUrl(Class clazz) {
		PageUrl pageUrl = getPageUrlAnnotation(clazz);
		Preconditions.checkArgument(Objects.nonNull(pageUrl) && StringUtils.isNotBlank(pageUrl.url()), "Url is not set for class " + clazz.getName());
		return pageUrl.url();
	}

	private static PageUrl getPageUrlAnnotation(Class clazz) {
		if (!clazz.isAnnotationPresent(PageUrl.class)) {
			return null;
		} else {
			return (PageUrl) clazz.getDeclaredAnnotation(PageUrl.class);
		}
	}
}
