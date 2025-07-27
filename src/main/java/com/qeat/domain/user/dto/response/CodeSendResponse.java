package com.qeat.domain.user.dto.response;

import lombok.Builder;

@Builder
public class CodeSendResponse {
    private final long expiresIn;
}
