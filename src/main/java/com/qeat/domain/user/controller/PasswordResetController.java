package com.qeat.domain.user.controller;

import com.qeat.domain.user.dto.request.PasswordResetRequest;
import com.qeat.domain.user.dto.response.PasswordResetResponse;
import com.qeat.global.mail.EmailService;
import com.qeat.global.mail.RedisEmailCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reset/password")
@RequiredArgsConstructor
public class PasswordResetController {

    private final EmailService emailService;
    private final RedisEmailCodeService redisEmailCodeService;

    private static final long EXPIRE_SECONDS = 240;

    @PostMapping("/request")
    public PasswordResetResponse sendResetCode(@RequestBody PasswordResetRequest request) {
        String email = request.email();

        String code = emailService.sendPasswordResetCode(email);
        redisEmailCodeService.saveCode(email, code, EXPIRE_SECONDS);

        return PasswordResetResponse.builder()
                .isSuccess(true)
                .code("200")
                .message("인증 코드가 전송되었습니다.")
                .result(PasswordResetResponse.Result.builder()
                        .expiresIn(EXPIRE_SECONDS)
                        .build())
                .build();
    }
}