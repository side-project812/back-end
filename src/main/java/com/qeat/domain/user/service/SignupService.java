package com.qeat.domain.user.service;

import com.qeat.domain.user.converter.UserConverter;
import com.qeat.domain.user.dto.request.SignupRequest;
import com.qeat.domain.user.dto.response.SignupResponse;
import com.qeat.domain.user.entity.User;
import com.qeat.domain.user.exception.code.UserErrorCode;
import com.qeat.domain.user.exception.handler.UserException;
import com.qeat.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
        }

        String encryptedPassword = passwordEncoder.encode(request.password());

        User user = userConverter.toEntity(request, encryptedPassword);
        User savedUser = userRepository.save(user);

        return new SignupResponse(savedUser.getId());
    }
}
