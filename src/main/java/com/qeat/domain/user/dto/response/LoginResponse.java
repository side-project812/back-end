package com.qeat.domain.user.dto.response;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        long expiresIn
) {}
