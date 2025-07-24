package com.qeat.domain.user.dto.request;

public record SignupRequest(
        String name,
        String email,
        String password
) {}