//package com.automation;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.support.AbstractFindByBuilder;
//import org.openqa.selenium.support.PageFactoryFinder;
//import org.openqa.selenium.support.pagefactory.Annotations;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Field;
//
////use old way/combine with new or add proxy interface create new interface
//public class CustomAnnotations extends Annotations{
//    private Field field;
//
//    public CustomAnnotations(Field field) {
//        super(field);
//        this.field = field;
//    }
//
//    @Override
//    public By buildBy() {
//        By ans = null;
//        Annotation[] var2 = this.field.getDeclaredAnnotations();
//        int var3 = var2.length;
//
//        for(int var4 = 0; var4 < var3; ++var4) {
//            Annotation annotation = var2[var4];
//            AbstractFindByBuilder builder = null;
//            if (annotation.annotationType().isAnnotationPresent(PageFactoryFinder.class)) {
//                try {
//                    builder = (AbstractFindByBuilder)((PageFactoryFinder)annotation.annotationType().getAnnotation(PageFactoryFinder.class)).value().newInstance();
//                } catch (ReflectiveOperationException var8) {
//                    ;
//                }
//            }
//
//            if (builder != null) {
//                ans = builder.buildIt(annotation, this.field);
//                break;
//            }
//        }
//
//        if (ans == null) {
//            ans = this.buildByFromDefault();
//        }
//
//        if (ans == null) {
//            throw new IllegalArgumentException("Cannot determine how to locate element " + this.field);
//        } else {
//            return ans;
//        }
//
//    }
//}
