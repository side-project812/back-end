package com.qeat.domain.user.service;

import com.qeat.domain.user.dto.request.OAuthRequest;
import com.qeat.domain.user.dto.response.OAuthResponse;
import com.qeat.domain.user.entity.User;
import com.qeat.domain.user.repository.OAuthRepository;
import com.qeat.global.security.JwtProvider;
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
        String provider = request.provider().toLowerCase(); // "google", "apple"
        OAuthUserInfoProvider userInfoProvider = oAuthUserInfoProviders.get(provider);

        if (userInfoProvider == null) {
            throw new IllegalArgumentException("Unsupported provider: " + provider);
        }

        OAuthUserInfo userInfo = userInfoProvider.getUserInfo(provider, request.token());

        return oAuthRepository.findByProviderAndProviderId(provider.toUpperCase(), userInfo.getProviderId())
                .map(user -> {
                    String jwt = jwtProvider.createToken(user.getId());
                    return new OAuthResponse(true, false, OAuthResponse.UserResponse.from(user), jwt);
                })
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .name(userInfo.getName())
                            .email(userInfo.getEmail())
                            .password("OAUTH_USER") // 소셜 로그인 유저는 비밀번호 없음
                            .provider(provider.toUpperCase())
                            .providerId(userInfo.getProviderId())
                            .avatar(userInfo.getAvatar())
                            .build();

                    oAuthRepository.save(newUser);
                    String jwt = jwtProvider.createToken(newUser.getId());
                    return new OAuthResponse(true, true, OAuthResponse.UserResponse.from(newUser), jwt);
                });
    }
}