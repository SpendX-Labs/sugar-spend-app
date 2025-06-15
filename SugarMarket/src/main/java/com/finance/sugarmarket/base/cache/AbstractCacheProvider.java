package com.finance.sugarmarket.base.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

public abstract class AbstractCacheProvider<T> implements ICacheProvider<T> {

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    private final Class<T> type;

    public AbstractCacheProvider(Class<T> type) {
        this.type = type;
    }

    @Override
    public T get(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }

    @Override
    public void put(String key, T value) {
        if (getExpire() == null) {
            redisTemplate.opsForValue().set(key, value);
        } else {
            redisTemplate.opsForValue().set(key, value, getExpire());
        }
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    //return null by overriding it if timeout is not required
    @Override
    public Duration getExpire() {
        return Duration.ofHours(5);
    }
}
