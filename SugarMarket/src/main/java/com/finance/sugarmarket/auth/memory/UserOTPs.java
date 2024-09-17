package com.finance.sugarmarket.auth.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.finance.sugarmarket.auth.model.OTPDetails;

public class UserOTPs {
    private static UserOTPs instance;

    private Map<String, OTPDetails> otpMap;
    private UserOTPs() {
        otpMap = new HashMap<>();
    }

    public static synchronized UserOTPs getInstance() {
        if (instance == null) {
            instance = new UserOTPs();
        }
        return instance;
    }

    public void addOtp(String userId, OTPDetails token) throws Exception {
    	if(otpMap.containsKey(userId) && otpMap.get(userId).getExpirationTime() > System.currentTimeMillis()) {
    		throw new Exception("user already exists");
    	}
        otpMap.put(userId, token);
    }

    public OTPDetails getOtp(String userId) {
        return otpMap.get(userId);
    }

    public void removeOtp(String userId) {
        otpMap.remove(userId);
    }
    
    public List<String> getExpiredUserIds() {
    	List<String> expiredUserIds = new ArrayList<>();
        Iterator<Map.Entry<String, OTPDetails>> iterator = otpMap.entrySet().iterator();
        
        while (iterator.hasNext()) {
            Map.Entry<String, OTPDetails> entry = iterator.next();
            if (entry.getValue().getExpirationTime() < System.currentTimeMillis()) {
                expiredUserIds.add(entry.getKey());
                iterator.remove();
            }
        }
        
        return expiredUserIds;
    	
    }
}