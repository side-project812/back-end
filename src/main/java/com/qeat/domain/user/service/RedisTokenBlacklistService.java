package com.qeat.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisTokenBlacklistService implements TokenBlacklistService {

    private final StringRedisTemplate redisTemplate;

    private static final String PREFIX = "blacklist:";

    @Override
    public void blacklist(String token, long expirationMillis) {
        long expirationSeconds = expirationMillis / 1000;
        redisTemplate.opsForValue().set(PREFIX + token, "blacklisted", expirationSeconds, TimeUnit.SECONDS);
    }

    @Override
    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(PREFIX + token));
    }
}