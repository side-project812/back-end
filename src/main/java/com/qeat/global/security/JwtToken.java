package com.qeat.global.security;

public record JwtToken(
        String accessToken,
        String refreshToken,
        long expiresIn // 단위: 초
) {}
