package com.qeat.domain.coupon.service;

import com.qeat.domain.coupon.dto.request.CouponRegisterRequest;
import com.qeat.domain.coupon.dto.response.CouponListResponse;
import com.qeat.domain.coupon.dto.response.CouponRegisterResponse;
import com.qeat.domain.coupon.entity.Coupon;
import com.qeat.domain.coupon.exception.code.CouponErrorCode;
import com.qeat.domain.coupon.repository.CouponRepository;
import com.qeat.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<CouponListResponse> getCouponList() {
        List<Coupon> coupons = couponRepository.findAll();

        return coupons.stream().map(coupon ->
                CouponListResponse.builder()
                        .couponId(coupon.getId())
                        .name(coupon.getName())
                        .discountAmount(coupon.getDiscountAmount())
                        .minOrderAmount(coupon.getMinOrderAmount())
                        .validFrom(coupon.getValidFrom().toString())
                        .validTo(coupon.getValidTo().toString())
                        .couponCode(coupon.getCouponCode())
                        .build()
        ).toList();
    }
}