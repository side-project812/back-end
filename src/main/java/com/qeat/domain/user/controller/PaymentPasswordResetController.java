package com.qeat.domain.user.controller;

import com.qeat.domain.user.dto.request.PaymentPasswordResetRequest;
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
}
