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
public class PaymentPasswordResetService {

    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;

    public void changePaymentPassword(Long userId, String rawPassword) {
        User user = loginRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        String encoded = passwordEncoder.encode(rawPassword);
        user.changePaymentPassword(encoded);
        loginRepository.save(user);
    }
}