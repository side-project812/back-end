package com.qeat.domain.user.service;

import com.qeat.domain.user.entity.User;
import com.qeat.domain.user.exception.code.UserErrorCode;
import com.qeat.domain.user.exception.handler.UserException;
import com.qeat.domain.user.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;

    public void changePassword(String email, String newPassword) {
        User user = loginRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.changePassword(encodedPassword); // 또는 user.setPassword(encodedPassword);
        loginRepository.save(user);
    }
}