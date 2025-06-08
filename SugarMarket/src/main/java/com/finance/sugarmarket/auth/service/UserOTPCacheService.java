package com.finance.sugarmarket.auth.service;

import com.finance.sugarmarket.auth.dto.UserTempData;
import com.finance.sugarmarket.base.enums.SignUpPrefixType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserOTPCacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveUserInCache(UserTempData userTempData, String username) {
        String redisKey = userTempData.getPrefix().name() + ":" + username;
        redisTemplate.opsForValue().set(redisKey, userTempData);
        redisTemplate.expire(redisKey, 10, TimeUnit.MINUTES);
    }

    public UserTempData getUserInCache(SignUpPrefixType prefix, String username) {
        String redisKey = prefix.name() + ":" + username;
        UserTempData userTempData = (UserTempData) redisTemplate.opsForValue().get(redisKey);
        return userTempData;
    }
}
