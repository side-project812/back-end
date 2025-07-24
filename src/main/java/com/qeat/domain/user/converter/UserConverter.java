package com.qeat.domain.user.converter;

import com.qeat.domain.user.dto.request.SignupRequest;
import com.qeat.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public User toEntity(SignupRequest request, String encodedPassword) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(encodedPassword)
                .provider("LOCAL")
                .providerId(null)
                .build();
    }
}
