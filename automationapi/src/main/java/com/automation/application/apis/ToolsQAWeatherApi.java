package com.automation.application.apis;

import com.automation.core.ApiWrapper;
import com.automation.core.Authorization;
import com.automation.environment.EnvironmentProperties;
import com.jayway.restassured.http.ContentType;

public class ToolsQAWeatherApi {
    final EnvironmentProperties environmentProperties;
    final ApiWrapper apiClient;

    public ToolsQAWeatherApi(ApiWrapper apiClient, EnvironmentProperties environmentProperties) {
        this.apiClient = apiClient;
        this.environmentProperties = environmentProperties;

        apiClient.buildRequestUrlWithRequiredPort(environmentProperties.getEnvironmentProperty("toolsqa.weather.api.port", true));
        apiClient.setAuthorizationType(Authorization.NONE);
        apiClient.clearHeaders();
        apiClient.setContentType(ContentType.JSON);
        apiClient.setAcceptType(ContentType.JSON);
    }
}
