package com.finance.sugarmarket.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.finance.sugarmarket.auth.service.UserJwtCacheService;
import com.finance.sugarmarket.auth.service.JwtService;
import com.finance.sugarmarket.constants.AppConstants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserJwtCacheService jwtCacheService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String authHeader = request.getHeader(AppConstants.AUTHORIZATION);
		if (authHeader == null || AppConstants.AUTH_URI.equals(request.getRequestURI())) {
			filterChain.doFilter(request, response);
			return;
		}

		final String jwt = jwtCacheService.extractJwtFromHeader(authHeader);
		
		final String userName = jwtService.extractUsername(jwt);

		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			try {
				UserDetails redisUserDetails = jwtCacheService.getUserDetailsByToken(jwt);
				if (redisUserDetails != null && jwtService.isTokenValid(jwt, redisUserDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(redisUserDetails,
							null, redisUserDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			} catch (Exception e) {
				throw new ServletException("JWT Token not found");
			}
		}

		filterChain.doFilter(request, response);
	}
}