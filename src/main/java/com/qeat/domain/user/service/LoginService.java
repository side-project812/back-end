package com.qeat.domain.user.service;

import com.qeat.domain.user.dto.request.LoginRequest;
import com.qeat.domain.user.dto.response.LoginResponse;
import com.qeat.domain.user.entity.User;
import com.qeat.domain.user.repository.LoginRepository;
import com.qeat.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public LoginResponse login(LoginRequest request) {
        User user = loginRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!user.getProvider().equals("LOCAL")) {
            throw new IllegalArgumentException("소셜 로그인 계정입니다.");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtProvider.createToken(user.getId());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());
        long expiresIn = jwtProvider.getAccessTokenExpiry(); // 초 단위

        return new LoginResponse(accessToken, refreshToken, expiresIn);
    }
}