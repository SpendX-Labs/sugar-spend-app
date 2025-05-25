package com.finance.sugarmarket.auth.cache;

import com.finance.sugarmarket.constants.AppConstants;
import com.finance.sugarmarket.constants.RedisConstants;

public class JWTCacheProvider extends AbstractCacheProvider<Long> {

    public String extractJwtFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    public JWTCacheProvider(Class<Long> type) {
        super(type);
    }

    @Override
    public String getFinalKey(String key) {
        return RedisConstants.JWT_TOKEN + AppConstants.HASH_DELIMITER + key;
    }

    public void saveTokenVsUserId(Long userId, String token) {
        put(getFinalKey(token), userId);
    }

    public Long getTokenVsUserId(String token) {
        return get(getFinalKey(token));
    }

    public void removeTokenVsUserId(String token) {
        remove(getFinalKey(token));
    }
}
