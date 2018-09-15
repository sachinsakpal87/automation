//package com.automation;
//
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.interactions.internal.Locatable;
//import org.openqa.selenium.internal.WrapsElement;
//import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
//import org.openqa.selenium.support.pagefactory.ElementLocator;
//import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
//import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Proxy;
//import java.util.List;
//
//public class CustomFieldDecorator extends DefaultFieldDecorator {
//
//    public CustomFieldDecorator(ElementLocatorFactory factory) {
//        super(factory);
//    }
//
//    public Object decorate(ClassLoader loader, Field field) {
//        if (!WebElement.class.isAssignableFrom(field.getType()) && !this.isDecoratableList(field)) {
//            return null;
//        } else {
//            ElementLocator locator = this.factory.createLocator(field);
//            if (locator == null) {
//                return null;
//            } else if (WebElement.class.isAssignableFrom(field.getType())) {
//                return this.proxyForLocator(loader, locator);
//            } else {
//                return List.class.isAssignableFrom(field.getType()) ? this.proxyForListLocator(loader, locator) : null;
//            }
//        }
//    }
//
//    protected WebElement proxyForLocator(ClassLoader loader, ElementLocator locator) {
//        InvocationHandler handler = new LocatingElementHandler(locator);
//        WebElement proxy = (WebElement) Proxy.newProxyInstance(loader, new Class[]{WebElement.class, WrapsElement.class, Locatable.class}, handler);
//        return proxy;
//    }
//
//}
