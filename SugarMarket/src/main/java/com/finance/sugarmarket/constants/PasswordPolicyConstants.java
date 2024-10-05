package com.finance.sugarmarket.constants;

public class PasswordPolicyConstants {

	public static final int MIN_PASSWORD_LENGTH = 8;

	public static final String UPPER_CASE_PATTERN = ".*[A-Z].*";
	public static final String LOWER_CASE_PATTERN = ".*[a-z].*";
	public static final String DIGIT_PATTERN = ".*[0-9].*";
	public static final String SPECIAL_CHAR_PATTERN = ".*[@#$%^&+=].*";

	public static final String ERROR_LENGTH = "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long.";
	public static final String ERROR_UPPER_CASE = "Password must contain at least one uppercase letter.";
	public static final String ERROR_LOWER_CASE = "Password must contain at least one lowercase letter.";
	public static final String ERROR_DIGIT = "Password must contain at least one digit.";
	public static final String ERROR_SPECIAL_CHAR = "Password must contain at least one special character (@#$%^&+=).";
}