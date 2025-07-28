package com.qeat.domain.coupon.service;

import com.qeat.domain.coupon.dto.request.CouponRegisterRequest;
import com.qeat.domain.coupon.dto.response.CouponRegisterResponse;
import com.qeat.domain.coupon.entity.Coupon;
import com.qeat.domain.coupon.exception.code.CouponErrorCode;
import com.qeat.domain.coupon.repository.CouponRepository;
import com.qeat.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponRegisterResponse registerCoupon(CouponRegisterRequest request) {
        Coupon coupon = couponRepository.findByCouponCode(request.couponCode())
                .orElseThrow(() -> new CustomException(CouponErrorCode.INVALID_COUPON));

        return CouponRegisterResponse.builder()
                .couponId(coupon.getId())
                .name(coupon.getName())
                .discountAmount(coupon.getDiscountAmount())
                .validFrom(coupon.getValidFrom().toString())
                .validTo(coupon.getValidTo().toString())
                .build();
    }
}