//package com.automation;
//
//import org.openqa.selenium.support.How;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
//@Retention(RetentionPolicy.RUNTIME)
//@Target({ElementType.FIELD, ElementType.TYPE})
////@PageFactoryFinder(CustomFindBy.FindByBuilder.class)
//public @interface CustomFindBy {
//
//    How how() default How.XPATH;
//    String xpath() default "";
//    String[] params() default {};
//
////    public static class FindByBuilder extends AbstractFindByBuilder {
////        public FindByBuilder() {
////        }
////
////        public By buildIt(Object annotation, Field field) {
////            CustomFindBy findBy = (CustomFindBy) annotation;
////            By ans = this.buildByFromShortFindBy(findBy);
////            if (ans == null) {
////                ans = this.buildByFromLongFindBy(findBy);
////            }
////
////            return ans;
////        }
////    }
//}
