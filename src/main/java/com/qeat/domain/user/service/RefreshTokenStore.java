package com.qeat.domain.user.service;

public interface RefreshTokenStore {
    void save(Long userId, String refreshToken, long expirationMillis);
    String get(Long userId);
    void delete(Long userId);
}
