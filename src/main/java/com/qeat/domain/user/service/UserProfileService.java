package com.qeat.domain.user.service;

import com.qeat.domain.user.entity.User;
import com.qeat.domain.user.exception.code.UserErrorCode;
import com.qeat.domain.user.exception.handler.UserException;
import com.qeat.domain.user.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final LoginRepository loginRepository;

    public void changeName(Long userId, String newName) {
        User user = loginRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        user.changeName(newName);
        loginRepository.save(user);
    }

    public void changeEmail(Long userId, String newEmail) {
        if (loginRepository.findByEmail(newEmail).isPresent()) {
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
        }

        User user = loginRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        user.changeEmail(newEmail);
        loginRepository.save(user);
    }
}
