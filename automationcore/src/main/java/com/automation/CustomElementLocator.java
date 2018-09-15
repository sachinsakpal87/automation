//package com.automation;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.SearchContext;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.pagefactory.ElementLocator;
//
//import java.util.List;
//
//public class CustomElementLocator implements ElementLocator {
//
//    private final SearchContext searchContext;
//    private final boolean shouldCache;
//    private final By by;
//    private WebElement cachedElement;
//    private List<WebElement> cachedElementList;
//
//
//    @Override
//    public WebElement findElement() {
//        return null;
//    }
//
//    @Override
//    public List<WebElement> findElements() {
//        return null;
//    }
//
//
////    public CustomElementLocator(SearchContext searchContext, Field field) {
////        this(searchContext, (AbstractAnnotations)(new Annotations(field)));
////    }
//
//
//    public CustomElementLocator(SearchContext searchContext, CustomAnnotations customAnnotation) {
//        this.searchContext = searchContext;
//        this.shouldCache = customAnnotation.isLookupCached();
//        this.by = customAnnotation.buildBy();
//    }
//}
