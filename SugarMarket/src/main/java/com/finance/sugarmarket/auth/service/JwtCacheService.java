package com.finance.sugarmarket.auth.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtCacheService {
	
	public String extractJwtFromHeader(String authHeader) {
	    if (authHeader != null && authHeader.startsWith("Bearer ")) {
	        return authHeader.substring(7);
	    }
	    return null;
	}
	
	@CachePut(value = "jwtTokens", key = "#jwtToken")
	public UserDetails saveUserToken(String jwtToken, UserDetails user) {
		return user;
	}

	@Cacheable(value = "jwtTokens", key = "#jwtToken")
	public UserDetails getUserDetailsByToken(String jwtToken) throws Exception {
		throw new Exception("Token not found");
	}

	@CacheEvict(value = "jwtTokens", key = "#jwtToken")
	public void removeToken(String jwtToken) {

	}

}
