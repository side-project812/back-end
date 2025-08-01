package com.qeat.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CodeSendResponse {
    private final long expiresIn;
}
