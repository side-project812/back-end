package com.qeat.domain.user.service;

import com.qeat.domain.user.dto.response.LoginResponse;
import com.qeat.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenRefreshService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenStore refreshTokenStore;

    public LoginResponse refresh(String refreshToken) {
        Long userId = jwtProvider.extractUserId(refreshToken);
        String storedToken = refreshTokenStore.get(userId);

        if (storedToken == null || !storedToken.equals(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        String newAccessToken = jwtProvider.createToken(userId);
        String newRefreshToken = jwtProvider.createRefreshToken(userId);
        refreshTokenStore.save(userId, newRefreshToken, jwtProvider.getRefreshTokenExpiry());

        return new LoginResponse(newAccessToken, newRefreshToken, jwtProvider.getAccessTokenExpiry());
    }
}