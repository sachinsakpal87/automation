package com.automation.enums;

import lombok.Getter;

@Getter
public enum URLS {
	HOME("HOME", "https://www.phptravels.net"),
	LOGIN("LOGIN", "https://www.phptravels.net/login"),
	ADMIN("ADMIN", "https://www.phptravels.net/admin"),
	SUPPLIER("SUPPLIER", "https://www.phptravels.net/supplier");

	private final String name;
	private final String url;

	URLS(String name, String url) {
		this.name = name;
		this.url = url;
	}
}
