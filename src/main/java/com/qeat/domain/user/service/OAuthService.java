package com.qeat.domain.user.service;

import com.qeat.domain.user.dto.request.OAuthRequest;
import com.qeat.domain.user.dto.response.OAuthResponse;
import com.qeat.domain.user.entity.User;
import com.qeat.domain.user.repository.OAuthRepository;
import com.qeat.global.security.JwtProvider;
import com.qeat.global.security.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final OAuthRepository oAuthRepository;
    private final JwtProvider jwtProvider;
    private final Map<String, OAuthUserInfoProvider> oAuthUserInfoProviders;

    public OAuthResponse loginOrSignup(OAuthRequest request) {
        String provider = request.provider().toLowerCase(); // "google"
        OAuthUserInfoProvider userInfoProvider = oAuthUserInfoProviders.get(provider);

        if (userInfoProvider == null) {
            throw new IllegalArgumentException("Unsupported provider: " + provider);
        }

        OAuthUserInfo userInfo = userInfoProvider.getUserInfo(provider, request.token());

        return oAuthRepository.findByProviderAndProviderId(provider.toUpperCase(), userInfo.getProviderId())
                .map(user -> {
                    JwtToken token = jwtProvider.createTokens(user.getId());
                    return new OAuthResponse(
                            true,
                            false,
                            OAuthResponse.UserResponse.from(user),
                            token.accessToken(),
                            token.refreshToken(),
                            token.expiresIn()
                    );
                })
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .name(userInfo.getName())
                            .email(userInfo.getEmail())
                            .password("OAUTH_USER")
                            .provider(provider.toUpperCase())
                            .providerId(userInfo.getProviderId())
                            .avatar(userInfo.getAvatar())
                            .build();

                    oAuthRepository.save(newUser);
                    JwtToken token = jwtProvider.createTokens(newUser.getId());
                    return new OAuthResponse(
                            true,
                            true,
                            OAuthResponse.UserResponse.from(newUser),
                            token.accessToken(),
                            token.refreshToken(),
                            token.expiresIn()
                    );
                });
    }
}