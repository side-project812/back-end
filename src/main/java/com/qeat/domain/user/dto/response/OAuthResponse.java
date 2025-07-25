package com.qeat.domain.user.dto.response;

import com.qeat.domain.user.entity.User;

public record OAuthResponse(
        boolean isSuccess,
        boolean isNewUser,
        UserResponse user,
        String token
) {
    public record UserResponse(
            Long id,
            String email,
            String name,
            String provider,
            String providerId,
            String avatar
    ) {
        public static UserResponse from(User user) {
            return new UserResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    user.getProvider(),
                    user.getProviderId(),
                    user.getAvatar()
            );
        }
    }
}
