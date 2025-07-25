package com.qeat.domain.user.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("google")
public class GoogleOAuthProvider implements OAuthUserInfoProvider {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public OAuthUserInfo getUserInfo(String provider, String token) {
        if (!provider.equalsIgnoreCase("google")) {
            throw new IllegalArgumentException("GoogleOAuthProvider only supports 'google'");
        }

        try {
            String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + token;
            String response = restTemplate.getForObject(url, String.class);
            JsonNode json = objectMapper.readTree(response);

            return OAuthUserInfo.builder()
                    .email(json.get("email").asText())
                    .name(json.get("name").asText())
                    .providerId(json.get("sub").asText())
                    .avatar(json.has("picture") ? json.get("picture").asText() : null)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to get Google user info", e);
        }
    }
}