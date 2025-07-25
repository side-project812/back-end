package com.qeat.domain.user.service;

import com.qeat.global.security.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService {

    private final TokenBlacklistService tokenBlacklistService;

    public void logout(String token) {
        long expireInMs = JwtTokenUtils.extractExpiration(token);
        if (expireInMs > 0) {
            tokenBlacklistService.blacklist(token, expireInMs);
        }
    }
}