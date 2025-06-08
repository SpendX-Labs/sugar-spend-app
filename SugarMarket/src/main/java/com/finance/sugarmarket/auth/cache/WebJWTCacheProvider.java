package com.finance.sugarmarket.auth.cache;

import org.springframework.stereotype.Service;

@Service
public class WebJWTCacheProvider extends JWTCacheProvider {
    public WebJWTCacheProvider() {
        super(Long.class);
    }
}
