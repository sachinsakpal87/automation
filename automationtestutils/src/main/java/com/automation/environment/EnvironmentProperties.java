package com.automation.environment;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class EnvironmentProperties {
    private Map<String, String> envProperties = new HashMap<>();

    public EnvironmentProperties() {
        readEnvironmentProperties();
    }

    private void readEnvironmentProperties() {
        String environmentName = System.getProperty("com/automation/environment");
        String propertyFile = this.getClass().getPackage().getName() + "." + (StringUtils.isNotBlank(environmentName) ? environmentName : "test");
        ResourceBundle rb = ResourceBundle.getBundle(propertyFile);

        for (String key : rb.keySet()) {
            envProperties.put(key, rb.getString(key));
        }
    }

    public String getEnvironmentProperty(String key, boolean isNullAllowed) {
        String property = envProperties.get(key);
        if (!isNullAllowed && property == null) {
            throw new IllegalStateException("Property not defined for " + key);
        }
        return property;
    }
}
