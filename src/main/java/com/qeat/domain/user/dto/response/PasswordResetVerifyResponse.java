package com.qeat.domain.user.dto.response;

import lombok.Builder;

@Builder
public class PasswordResetVerifyResponse {
    private final int expiresIn;
}
