package com.qeat.domain.user.dto.request;

public record PasswordResetChangeRequest(
        String email,
        String password
) {}
