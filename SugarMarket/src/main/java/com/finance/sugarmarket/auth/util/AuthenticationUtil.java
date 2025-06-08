package com.finance.sugarmarket.auth.util;

import com.finance.sugarmarket.constants.AppConstants;
import com.finance.sugarmarket.constants.PasswordPolicyConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthenticationUtil {
    private static AuthenticationUtil instance = new AuthenticationUtil();

    private AuthenticationUtil() {
    }

    public static AuthenticationUtil getInstance() {
        return instance;
    }

    public boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(AppConstants.EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public String checkPasswordPolicy(String password) {
        // Check if the password meets the length requirement
        if (password.length() < PasswordPolicyConstants.MIN_PASSWORD_LENGTH) {
            return PasswordPolicyConstants.ERROR_LENGTH;
        }

        // Check if the password contains at least one uppercase letter
        if (!password.matches(PasswordPolicyConstants.UPPER_CASE_PATTERN)) {
            return PasswordPolicyConstants.ERROR_UPPER_CASE;
        }

        // Check if the password contains at least one lowercase letter
        if (!password.matches(PasswordPolicyConstants.LOWER_CASE_PATTERN)) {
            return PasswordPolicyConstants.ERROR_LOWER_CASE;
        }

        // Check if the password contains at least one digit
        if (!password.matches(PasswordPolicyConstants.DIGIT_PATTERN)) {
            return PasswordPolicyConstants.ERROR_DIGIT;
        }

        // Check if the password contains at least one special character
        if (!password.matches(PasswordPolicyConstants.SPECIAL_CHAR_PATTERN)) {
            return PasswordPolicyConstants.ERROR_SPECIAL_CHAR;
        }

        // If all checks pass
        return AppConstants.SUCCESS;
    }
}