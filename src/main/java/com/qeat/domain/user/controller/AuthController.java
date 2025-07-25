package com.qeat.domain.user.controller;

import com.qeat.domain.user.dto.request.LoginRequest;
import com.qeat.domain.user.dto.request.OAuthRequest;
import com.qeat.domain.user.dto.request.SignupRequest;
import com.qeat.domain.user.dto.response.LoginResponse;
import com.qeat.domain.user.dto.response.OAuthResponse;
import com.qeat.domain.user.dto.response.SignupResponse;
import com.qeat.domain.user.service.LoginService;
import com.qeat.domain.user.service.OAuthService;
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
    private final OAuthService oAuthService;
    private final LoginService loginService;

    @PostMapping("/signup")
    @Operation(summary = "이메일 회원가입 API", description = "이메일 회원가입 요청 처리")
    public ResponseEntity<CustomResponse<SignupResponse>> signup(@RequestBody SignupRequest request) {
        SignupResponse response = signupService.signup(request);
        return ResponseEntity.ok(CustomResponse.onSuccess(response));
    }

    @PostMapping("/oauth")
    @Operation(summary = "구글/애플 회원가입 및 로그인 API", description = "소셜 회원가입 및 로그인 요청 처리")
    public ResponseEntity<CustomResponse<OAuthResponse>> oauth(@RequestBody OAuthRequest request) {
        OAuthResponse response = oAuthService.loginOrSignup(request);
        return ResponseEntity.ok(CustomResponse.onSuccess(response));
    }

    @PostMapping("/login")
    @Operation(summary = "이메일 로그인 API", description = "이메일 로그인 요청 처리")
    public ResponseEntity<CustomResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        LoginResponse response = loginService.login(request);
        return ResponseEntity.ok(CustomResponse.onSuccess(response));
    }
}
