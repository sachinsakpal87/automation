//package com.automation;
//
//import org.openqa.selenium.SearchContext;
//import org.openqa.selenium.support.pagefactory.Annotations;
//import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
//import org.openqa.selenium.support.pagefactory.ElementLocator;
//import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
//
//import java.lang.reflect.Field;
//
//public class CustomElementLocatorFactory implements ElementLocatorFactory {
//    private final SearchContext searchContext;
//
//    public CustomElementLocatorFactory(SearchContext searchContext) {
//        this.searchContext = searchContext;
//    }
//
//    @Override
//    public ElementLocator createLocator(Field field) {
//        CustomFindBy customannotation = field.getAnnotation(CustomFindBy.class);
//        if(customannotation != null){
//            return new CustomElementLocator(this.searchContext, new CustomAnnotations(field));
//        }else {
//            return new DefaultElementLocator(this.searchContext, new Annotations(field));
//        }
//    }
//}
