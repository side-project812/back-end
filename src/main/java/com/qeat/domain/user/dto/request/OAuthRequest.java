package com.qeat.domain.user.dto.request;

public record OAuthRequest(
        String provider, // "google" 또는 "apple"
        String token
) {}
