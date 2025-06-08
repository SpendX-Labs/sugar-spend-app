package com.finance.sugarmarket.auth.config;

import com.finance.sugarmarket.auth.cache.UserCacheProvider;
import com.finance.sugarmarket.auth.cache.WebJWTCacheProvider;
import com.finance.sugarmarket.auth.dto.UserDetailsDTO;
import com.finance.sugarmarket.auth.service.JwtService;
import com.finance.sugarmarket.constants.AppConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private WebJWTCacheProvider webJWTCacheProvider;

    @Autowired
    private UserCacheProvider userCacheProvider;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader(AppConstants.AUTHORIZATION);
        if (authHeader == null || AppConstants.AUTH_URI.equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = webJWTCacheProvider.extractJwtFromHeader(authHeader);

        final String userName = jwtService.extractUsername(jwt);

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            try {
                UserDetailsDTO redisUserDetails = userCacheProvider.getUserDetails(webJWTCacheProvider.getTokenVsUserId(jwt));
                if (redisUserDetails != null && jwtService.isTokenValid(jwt, redisUserDetails)) {
                    UserPrincipal userPrincipal = new UserPrincipal(redisUserDetails.getId(), redisUserDetails.getUsername(), null);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userPrincipal, null, userPrincipal.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                logger.error("JWT token validation failed: ", e);
            }
        }

        filterChain.doFilter(request, response);
    }
}