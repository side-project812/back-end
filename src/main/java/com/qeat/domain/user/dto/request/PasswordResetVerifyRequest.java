package com.qeat.domain.user.dto.request;

public record PasswordResetVerifyRequest(
        String email,
        String verificationCode
) {}
