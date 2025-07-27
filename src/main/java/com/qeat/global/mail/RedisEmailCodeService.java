package com.qeat.global.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisEmailCodeService {

    private final StringRedisTemplate redisTemplate;
    private static final String PREFIX = "reset:password:";

    public void saveCode(String email, String code, long ttlSeconds) {
        redisTemplate.opsForValue().set(PREFIX + email, code, ttlSeconds, TimeUnit.SECONDS);
    }

    public void savePaymentCode(String email, String code, long ttlSeconds) {
        redisTemplate.opsForValue().set("reset:payment:" + email, code, ttlSeconds, TimeUnit.SECONDS);
    }

    public String getCode(String email) {

        return redisTemplate.opsForValue().get(PREFIX + email);
    }

    public String getPaymentCode(String email) {
        return redisTemplate.opsForValue().get("reset:payment:" + email);
    }

    public void deleteCode(String email) {

        redisTemplate.delete(PREFIX + email);
    }
}
