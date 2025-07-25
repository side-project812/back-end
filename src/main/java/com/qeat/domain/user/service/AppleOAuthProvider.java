package com.qeat.domain.user.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component("apple")
public class AppleOAuthProvider implements OAuthUserInfoProvider {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public OAuthUserInfo getUserInfo(String provider, String token) {
        if (!provider.equalsIgnoreCase("apple")) {
            throw new IllegalArgumentException("AppleOAuthProvider only supports 'apple'");
        }

        try {
            // id_token은 JWT이므로 '.'으로 구분된 3개의 Base64 파트로 구성
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                throw new IllegalArgumentException("Invalid Apple ID Token");
            }

            // payload 부분(Base64 디코딩)
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            JsonNode json = objectMapper.readTree(payload);

            String email = json.has("email") ? json.get("email").asText() : "unknown@apple.com";
            String sub = json.get("sub").asText(); // Apple의 고유 사용자 식별자

            return OAuthUserInfo.builder()
                    .email(email)
                    .name("Apple User") // 애플은 name 정보를 주지 않음
                    .providerId(sub)
                    .avatar(null)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Apple id_token", e);
        }
    }
}