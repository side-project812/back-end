package com.qeat.domain.user.controller;

import com.qeat.domain.user.dto.request.SignupRequest;
import com.qeat.domain.user.dto.response.SignupResponse;
import com.qeat.domain.user.service.SignupService;
import com.qeat.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "회원가입/로그인/로그아웃")
@RequiredArgsConstructor
public class AuthController {

    private final SignupService signupService;

    @PostMapping("/signup")
    @Operation(summary = "이메일 회원가입 API", description = "이메일 회원가입 요청을 처리합니다.")
    public ResponseEntity<CustomResponse<SignupResponse>> signup(@RequestBody SignupRequest request) {
        SignupResponse response = signupService.signup(request);
        return ResponseEntity.ok(CustomResponse.onSuccess(response));
    }

}
