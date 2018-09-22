package com.automation;


import java.util.Objects;

class RuntimeParameters {

    private RuntimeParameters(){
        throw new UnsupportedOperationException();
    }
    public static String getSystemProperty(String property, String defaultValue) {
        if (Objects.isNull(System.getProperty(property)))
            return defaultValue;
        return System.getProperty(property);
    }
}
