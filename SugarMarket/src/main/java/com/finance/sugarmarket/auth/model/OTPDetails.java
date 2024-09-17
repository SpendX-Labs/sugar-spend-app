package com.finance.sugarmarket.auth.model;

public class OTPDetails {
    private String otp;
    private long expirationTime;

    public OTPDetails(String otp, long expirationTime) {
        this.otp = otp;
        this.expirationTime = expirationTime;
    }

    public String getOtp() {
        return otp;
    }

    public long getExpirationTime() {
        return expirationTime;
    }
}