package com.qeat.domain.coupon.dto.response;

public record UseCouponResponse(
        Long couponId,
        int discountAmount
) {}
