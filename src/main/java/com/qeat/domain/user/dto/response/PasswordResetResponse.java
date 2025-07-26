package com.qeat.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PasswordResetResponse {
    private boolean isSuccess;
    private String code;
    private String message;
    private Result result;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Result {
        private long expiresIn;
    }
}