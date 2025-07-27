package com.qeat.domain.user.dto.request;

public record PaymentPasswordVerifyRequest(
        String email,
        String verificationCode
) {}
