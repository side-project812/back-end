package com.qeat.domain.coupon.controller;

import com.qeat.domain.coupon.dto.request.CouponRegisterRequest;
import com.qeat.domain.coupon.dto.request.UseCouponRequest;
import com.qeat.domain.coupon.dto.response.CouponListResponse;
import com.qeat.domain.coupon.dto.response.CouponRegisterResponse;
import com.qeat.domain.coupon.dto.response.UseCouponResponse;
import com.qeat.domain.coupon.service.CouponService;
import com.qeat.global.apiPayload.CustomResponse;
import com.qeat.global.apiPayload.exception.CustomException;
import com.qeat.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupon")
@Tag(name = "쿠폰")
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/register")
    @Operation(summary = "쿠폰 등록 API", description = "쿠폰 코드를 입력 받아 쿠폰 등록")
    public ResponseEntity<CustomResponse<CouponRegisterResponse>> registerCoupon(
            @RequestBody CouponRegisterRequest request
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getUserId();

        CouponRegisterResponse result = couponService.registerCoupon(userId, request);
        return ResponseEntity.ok(CustomResponse.onSuccess(result, "쿠폰이 등록되었습니다."));
    }

    @PostMapping("/use")
    @Operation(summary = "쿠폰 사용 API", description = "보유한 쿠폰을 사용 처리합니다.")
    public ResponseEntity<CustomResponse<UseCouponResponse>> useCoupon(
            @RequestBody UseCouponRequest request
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getUserId();

        UseCouponResponse result = couponService.useCoupon(userId, request);
        return ResponseEntity.ok(CustomResponse.onSuccess(result));
    }

    @GetMapping("/list")
    @Operation(summary = "쿠폰 리스트 조회 API", description = "내 쿠폰 목록을 조회합니다.")
    public ResponseEntity<CustomResponse<List<CouponListResponse>>> getCouponList() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getUserId();

        List<CouponListResponse> result = couponService.getCouponList(userId);
        return ResponseEntity.ok(CustomResponse.onSuccess(result));
    }
}