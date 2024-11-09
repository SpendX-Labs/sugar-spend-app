package com.finance.sugarmarket.auth.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.finance.sugarmarket.auth.config.UserPrincipal;
import com.finance.sugarmarket.constants.AppConstants;

@Service
public class UserJwtCacheService {

	private static final String TOKEN_MID = "_JWT_TOKEN:";
	private static final String ID_PREFIX = "_JWT_TOKEN:*";
	private static final String MOBILE_TOKEN_MID = "_MOBILE_TOKEN:";

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	public String extractJwtFromHeader(String authHeader) {
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		return null;
	}

	public void saveUserToken(String jwtToken, UserDetails user, String loggedInBy) {
		Long userId = null;
		if (user instanceof UserPrincipal) {
			userId = ((UserPrincipal) user).getId();

		}
		if (!AppConstants.MOBILE.equals(loggedInBy)) {
			String redisKey = generateKey(jwtToken, userId);
			redisTemplate.opsForValue().set(redisKey, user);
			redisTemplate.expire(redisKey, 24, TimeUnit.HOURS);
		} else {
			removeExistingMobileToken(userId);
			String redisKey = generateMobileKey(jwtToken, userId);
			redisTemplate.opsForValue().set(redisKey, user);
		}
	}

	public void updateAllCacheKeyValues(Long userId, UserDetails user) throws Exception {
		String pattern = userId + ID_PREFIX;
		Set<String> keys = redisTemplate.keys(pattern);
		if (keys == null || keys.isEmpty()) {
			throw new Exception("Token not found");
		}

		for (String key : keys) {
			redisTemplate.opsForValue().set(key, user);
		}
	}

	public UserDetails getUserDetailsByToken(String jwtToken) throws Exception {

		String redisKey = getRedisKeyByPattern(jwtToken);

		UserDetails userDetails = (UserDetails) redisTemplate.opsForValue().get(redisKey);

		if (userDetails == null) {
			throw new Exception("Token not found");
		}

		return userDetails;
	}

	public void removeToken(String jwtToken) throws Exception {
		String redisKey = getRedisKeyByPattern(jwtToken);
		redisTemplate.delete(redisKey);
	}

	private String getRedisKeyByPattern(String jwtToken) throws Exception {
		String pattern = "*:" + jwtToken;
		Set<String> keys = redisTemplate.keys(pattern);

		if (keys == null || keys.isEmpty()) {
			throw new Exception("Token not found");
		}

		return keys.iterator().next();
	}

	private String generateKey(String jwtToken, Long userId) {
		if (userId == null)
			userId = -1L;
		return userId + TOKEN_MID + jwtToken;
	}

	private String generateMobileKey(String jwtToken, Long userId) {
		if (userId == null)
			userId = -1L;
		return userId + MOBILE_TOKEN_MID + jwtToken;
	}

	private void removeExistingMobileToken(Long userId) {
		String pattern = userId + MOBILE_TOKEN_MID + "*";
		Set<String> keys = redisTemplate.keys(pattern);
		redisTemplate.delete(keys);
	}

}
