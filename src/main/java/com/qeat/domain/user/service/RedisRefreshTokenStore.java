package com.qeat.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisRefreshTokenStore implements RefreshTokenStore {

    private final StringRedisTemplate redisTemplate;
    private static final String PREFIX = "refresh:";

    @Override
    public void save(Long userId, String refreshToken, long expirationMillis) {
        redisTemplate.opsForValue().set(PREFIX + userId, refreshToken, expirationMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public String get(Long userId) {
        return redisTemplate.opsForValue().get(PREFIX + userId);
    }

    @Override
    public void delete(Long userId) {
        redisTemplate.delete(PREFIX + userId);
    }
}