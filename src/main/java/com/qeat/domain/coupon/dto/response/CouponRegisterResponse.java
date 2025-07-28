package com.qeat.domain.coupon.dto.response;

import lombok.Builder;

@Builder
public record CouponRegisterResponse(
        Long couponId,
        String name,
        int discountAmount,
        String validFrom,
        String validTo
) {}