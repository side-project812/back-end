package com.qeat.domain.user.dto.request;

public record OAuthRequest(
        String provider,
        String token
) {}
