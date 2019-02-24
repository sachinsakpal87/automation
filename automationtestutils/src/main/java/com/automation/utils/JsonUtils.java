package com.automation.utils;

import java.net.URL;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

	public static <T> String deserializeToJsonString(T data) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			mapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE);
			mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
			mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
			return mapper.writeValueAsString(data);
		} catch (Exception e) {
			throw new IllegalStateException("Error while deserializing to json", e);
		}
	}

	public static <T> T buildObjectFromJsonString(String json, Class<T> objectType) {
		if (json == null) {
			throw new IllegalArgumentException("Input json is null");
		} else {
			try {
				ObjectMapper mapper = new ObjectMapper();
				return mapper.readValue(json, objectType);
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
	}

	public static <T> T buildObjectFromJsonFile(URL url, Class<T> objectType) {
		if (url == null) {
			throw new IllegalArgumentException("Input file url is null");
		} else {
			try {
				ObjectMapper mapper = new ObjectMapper();
				return mapper.readValue(url, objectType);
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
	}
}