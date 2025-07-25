package com.qeat.domain.user.controller;

import com.qeat.domain.user.dto.request.LoginRequest;
import com.qeat.domain.user.dto.request.OAuthRequest;
import com.qeat.domain.user.dto.request.RefreshTokenRequest;
import com.qeat.domain.user.dto.request.SignupRequest;
import com.qeat.domain.user.dto.response.LoginResponse;
import com.qeat.domain.user.dto.response.OAuthResponse;
import com.qeat.domain.user.dto.response.SignupResponse;
import com.qeat.domain.user.service.*;
import com.qeat.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "회원가입/로그인/로그아웃")
@RequiredArgsConstructor
public class AuthController {

    private final SignupService signupService;
    private final OAuthService oAuthService;
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final TokenRefreshService tokenRefreshService;

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

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API", description = "로그아웃 요청 처리")
    public ResponseEntity<CustomResponse<Void>> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        logoutService.logout(token);
        return ResponseEntity.ok(CustomResponse.<Void>onSuccess(null, "로그아웃 되었습니다."));
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급 API", description = "refreshToken으로 새로운 access/refresh 토큰을 발급")
    public ResponseEntity<CustomResponse<LoginResponse>> refresh(@RequestBody RefreshTokenRequest request) {
        LoginResponse response = tokenRefreshService.refresh(request.refreshToken());
        return ResponseEntity.ok(CustomResponse.onSuccess(response, "토큰이 재발급되었습니다."));
    }
}
