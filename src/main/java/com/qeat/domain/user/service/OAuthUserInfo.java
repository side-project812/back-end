package com.qeat.domain.user.service;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthUserInfo {
    private String email;
    private String name;
    private String providerId;
    private String avatar;
}