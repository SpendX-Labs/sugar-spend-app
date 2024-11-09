package com.finance.sugarmarket.auth.enums;

import java.util.Arrays;

public enum UserPropertiesEnum {
	SMS_LOADED;

	public static String[] getAllValues() {
		return Arrays.stream(UserPropertiesEnum.values()).map(Enum::name).toArray(String[]::new);
	}
}
