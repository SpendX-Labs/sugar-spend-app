package com.finance.sugarmarket.auth.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.finance.sugarmarket.constants.AppConstants;

public class AuthenticationUtil {
    private static AuthenticationUtil instance = new AuthenticationUtil();
    
    private AuthenticationUtil() {}
    
    public static AuthenticationUtil getInstance() {
        return instance;
    }

    public boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(AppConstants.EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}