package com.qeat.domain.user.dto.request;

public record LoginRequest(
        String email,
        String password
) {}
