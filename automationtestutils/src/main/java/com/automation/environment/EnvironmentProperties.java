package com.automation.environment;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

public class EnvironmentProperties {
	private Map<String, String> environmentProperties = new HashMap<>();

	public EnvironmentProperties() {
		readEnvironmentProperties();
	}

	private void readEnvironmentProperties() {
		String environmentName = System.getProperty("com/automation/environment");
		String propertyFile = this.getClass().getPackage().getName() + "." + (StringUtils.isNotBlank(environmentName) ? environmentName : "test");
		ResourceBundle rb = ResourceBundle.getBundle(propertyFile);

		for (String key : rb.keySet()) {
			environmentProperties.put(key, rb.getString(key));
		}
	}

	public String getEnvironmentProperty(String key, boolean isNullAllowed) {
		String property = environmentProperties.get(key);
		if (!isNullAllowed && property == null) {
			throw new IllegalStateException("Property not defined for " + key);
		}
		return property;
	}
}