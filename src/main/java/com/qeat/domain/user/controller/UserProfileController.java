package com.qeat.domain.user.controller;

import com.qeat.domain.user.dto.request.ChangeNameRequest;
import com.qeat.domain.user.service.UserProfileService;
import com.qeat.global.apiPayload.CustomResponse;
import com.qeat.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.qeat.domain.user.dto.request.ChangeEmailRequest;

@RestController
@RequestMapping("/api/user/profile")
@Tag(name = "닉네임/이메일 변경")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PatchMapping("/name")
    @Operation(summary = "닉네임 변경 API", description = "닉네임 변경 요청 처리")
    public ResponseEntity<CustomResponse<Void>> changeName(
            @RequestBody ChangeNameRequest request
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getUserId();

        userProfileService.changeName(userId, request.name());

        return ResponseEntity.ok(
                CustomResponse.<Void>onSuccess(null, "닉네임이 변경되었습니다.")
        );
    }

    @PatchMapping("/email")
    @Operation(summary = "이메일 주소 변경 API", description = "사용자의 이메일 주소를 변경합니다.")
    public ResponseEntity<CustomResponse<Void>> changeEmail(
            @RequestBody ChangeEmailRequest request
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getUserId();

        userProfileService.changeEmail(userId, request.newEmail());

        return ResponseEntity.ok(
                CustomResponse.<Void>onSuccess(null, "이메일 주소가 변경되었습니다.")
        );
    }
}
