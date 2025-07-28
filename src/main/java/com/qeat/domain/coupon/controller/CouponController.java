package com.qeat.domain.coupon.controller;

import com.qeat.domain.coupon.dto.request.CouponRegisterRequest;
import com.qeat.domain.coupon.dto.response.CouponRegisterResponse;
import com.qeat.domain.coupon.service.CouponService;
import com.qeat.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupon")
@Tag(name = "쿠폰")
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/register")
    @Operation(summary = "쿠폰 등록 API", description = "쿠폰 코드를 입력 받아 쿠폰 등록")
    public ResponseEntity<CustomResponse<CouponRegisterResponse>> registerCoupon(@RequestBody CouponRegisterRequest request) {
        CouponRegisterResponse result = couponService.registerCoupon(request);
        return ResponseEntity.ok(CustomResponse.onSuccess(result, "쿠폰이 등록되었습니다."));
    }
}