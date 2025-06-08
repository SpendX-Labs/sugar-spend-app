package com.finance.sugarmarket.auth.dto;

import com.finance.sugarmarket.auth.model.MFUser;
import com.finance.sugarmarket.base.enums.SignUpPrefixType;

import java.io.Serializable;

public class UserTempData implements Serializable {
    private static final long serialVersionUID = 1L;
    private MFUser user;
    private String email;
    private String password;
    private String otp;
    private SignUpPrefixType prefix;

    public MFUser getUser() {
        return user;
    }

    public void setUser(MFUser user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public SignUpPrefixType getPrefix() {
        return prefix;
    }

    public void setPrefix(SignUpPrefixType prefix) {
        this.prefix = prefix;
    }

    // user
    public UserTempData(MFUser user, String otp) {
        super();
        this.user = user;
        this.otp = otp;
        this.prefix = SignUpPrefixType.USER_SIGNUP;
    }

    // email or
    public UserTempData(String data, String otp, SignUpPrefixType prefix) {
        super();
        if (prefix.equals(SignUpPrefixType.UPDATE_EMAIL)) {
            this.email = data;
        } else {
            this.password = data;
        }
        this.otp = otp;
        this.prefix = prefix;
    }

    public UserTempData() {
        super();
    }
}
