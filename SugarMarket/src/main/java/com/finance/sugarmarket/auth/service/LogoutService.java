package com.finance.sugarmarket.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.finance.sugarmarket.constants.AppConstants;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class LogoutService implements LogoutHandler {

	private static final Logger log = LoggerFactory.getLogger(LogoutService.class);

	@Autowired
	private UserJwtCacheService jwtCacheService;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		try {
			log.info("inside LogoutService()");
			final String authHeader = request.getHeader(AppConstants.AUTHORIZATION);
			final String jwt = jwtCacheService.extractJwtFromHeader(authHeader);
			jwtCacheService.removeToken(jwt);
		} catch (Exception e) {
			log.error("error while doing logout", e);
		}

	}
}