package com.qeat.domain.user.service;

import com.qeat.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService {

    private final TokenBlacklistService tokenBlacklistService;
    private final JwtProvider jwtProvider;

    public void logout(String token) {
        long expireInMs = jwtProvider.extractExpiration(token);
        if (expireInMs > 0) {
            tokenBlacklistService.blacklist(token, expireInMs);
        }
    }
}