package com.qeat.domain.user.controller;

import com.qeat.domain.user.dto.request.PaymentPasswordResetRequest;
import com.qeat.domain.user.dto.request.PaymentPasswordVerifyRequest;
import com.qeat.domain.user.dto.response.CodeSendResponse;
import com.qeat.global.apiPayload.CustomResponse;
import com.qeat.global.mail.EmailService;
import com.qeat.global.mail.RedisEmailCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reset/paymentPassword")
@Tag(name = "결제 비밀번호 재설정")
@RequiredArgsConstructor
public class PaymentPasswordResetController {
    private final EmailService emailService;
    private final RedisEmailCodeService redisEmailCodeService;

    private static final long EXPIRE_SECONDS = 240;

    @PostMapping("/request")
    @Operation(summary = "결제 비밀번호 재설정 코드 전송 요청 API", description = "결제 비밀번호 재설정 코드 전송 요청 처리")
    public ResponseEntity<CustomResponse<CodeSendResponse>> sendPaymentPasswordResetCode(
            @RequestBody PaymentPasswordResetRequest request,
            @RequestHeader("Authorization") String authHeader) {

        String code = emailService.sendPaymentPasswordResetCode(request.email());
        redisEmailCodeService.savePaymentCode(request.email(), code, EXPIRE_SECONDS);

        return ResponseEntity.ok(
                CustomResponse.onSuccess(
                        CodeSendResponse.builder().expiresIn(EXPIRE_SECONDS).build(),
                        "인증 코드가 전송되었습니다."
                )
        );
    }

    @PostMapping("/verify")
    @Operation(summary = "결제 비밀번호 코드 인증 API", description = "결제 비밀번호 코드 인증 요청 처리")
    public ResponseEntity<CustomResponse<Void>> verifyPaymentPasswordCode(
            @RequestBody PaymentPasswordVerifyRequest request,
            @RequestHeader("Authorization") String authHeader) {

        String storedCode = redisEmailCodeService.getPaymentCode(request.email());

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

        // 인증 성공 → 필요시 Redis에서 코드 삭제 가능
        return ResponseEntity.ok(
                CustomResponse.<Void>onSuccess(null, "인증 코드가 유효합니다.")
        );
    }
}
