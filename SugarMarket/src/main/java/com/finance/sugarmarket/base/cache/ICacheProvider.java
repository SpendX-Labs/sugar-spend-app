package com.finance.sugarmarket.base.cache;

import java.time.Duration;

public interface ICacheProvider<T> {
    String getFinalKey(String key);

    T get(String key);

    void put(String key, T value);

    void remove(String key);

    Duration getExpire();
}
