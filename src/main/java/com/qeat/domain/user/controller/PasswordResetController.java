package com.qeat.domain.user.controller;

import com.qeat.domain.user.dto.request.PasswordResetChangeRequest;
import com.qeat.domain.user.dto.request.PasswordResetRequest;
import com.qeat.domain.user.dto.request.PasswordResetVerifyRequest;
import com.qeat.domain.user.dto.response.PasswordResetResponse;
import com.qeat.domain.user.service.PasswordResetService;
import com.qeat.global.apiPayload.CustomResponse;
import com.qeat.global.mail.EmailService;
import com.qeat.global.mail.RedisEmailCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reset/password")
@Tag(name = "비밀번호 재설정")
@RequiredArgsConstructor
public class PasswordResetController {

    private final EmailService emailService;
    private final RedisEmailCodeService redisEmailCodeService;
    private final PasswordResetService passwordResetService;

    private static final long EXPIRE_SECONDS = 240;

    @PostMapping("/request")
    @Operation(summary = "비밀번호 재설정 코드 전송 요청 API", description = "비밀번호 재설정 코드 전송 요청 처리")
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

    @PostMapping("/verify")
    @Operation(summary = "비밀번호 재설정 코드 인증 API", description = "비밀번호 재설정 코드 인증 요청 처리")
    public ResponseEntity<CustomResponse<Void>> verifyResetCode(
            @RequestBody PasswordResetVerifyRequest request) {

        String storedCode = redisEmailCodeService.getCode(request.email());

        if (storedCode == null) {
            return ResponseEntity.badRequest().body(
                    CustomResponse.onFailure("400", "인증 코드가 만료되었거나 존재하지 않습니다.")
            );
        }

        if (!storedCode.equals(request.verificationCode())) {
            return ResponseEntity.status(401).body(
                    CustomResponse.onFailure("401", "인증 코드가 일치하지 않습니다.")
            );
        }

        // 인증 성공 시
        return ResponseEntity.ok(CustomResponse.<Void>onSuccess(null, "인증 코드가 유효합니다."));
    }

    @PostMapping("/change")
    @Operation(summary = "비밀번호 재설정 API", description = "비밀번호 재설정 요청 처리")
    public ResponseEntity<CustomResponse<Void>> changePassword(
            @RequestBody PasswordResetChangeRequest request) {

        passwordResetService.changePassword(request.email(), request.password());

        return ResponseEntity.ok(
                CustomResponse.<Void>onSuccess(null, "비밀번호가 변경되었습니다.")
        );
    }
}