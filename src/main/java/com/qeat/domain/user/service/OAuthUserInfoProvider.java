package com.qeat.domain.user.service;

public interface OAuthUserInfoProvider {
    OAuthUserInfo getUserInfo(String provider, String token);
}