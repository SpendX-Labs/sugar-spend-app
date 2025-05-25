package com.finance.sugarmarket.auth.cache;

import com.finance.sugarmarket.constants.AppConstants;
import com.finance.sugarmarket.constants.RedisConstants;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class MobileJWTCacheProvider extends JWTCacheProvider {
    public MobileJWTCacheProvider() {
        super(Long.class);
    }

    @Override
    public Duration getExpire() {
        return null;
    }

    @Override
    public void saveTokenVsUserId(Long userId, String token) {
        String oneLogInMobile = RedisConstants.MOBILE_ONE_LOGIN + AppConstants.HASH_DELIMITER + userId;
        String currentJwtKey = (String) redisTemplate.opsForValue().get(oneLogInMobile);
        if (currentJwtKey != null) {
            super.removeTokenVsUserId(currentJwtKey);
        }
        super.saveTokenVsUserId(userId, token);
        redisTemplate.opsForValue().set(oneLogInMobile, token);
    }
}
