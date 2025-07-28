package com.qeat.domain.coupon.dto.response;

import lombok.Builder;

@Builder
public record CouponListResponse(
        Long couponId,
        String name,
        Integer discountAmount,
        Integer minOrderAmount,
        String validFrom,
        String validTo,
        String couponCode
) {}