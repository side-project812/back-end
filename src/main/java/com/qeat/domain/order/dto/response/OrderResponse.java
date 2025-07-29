package com.qeat.domain.order.dto.response;

import lombok.Builder;

@Builder
public record OrderResponse(
        Long orderId,
        int originalAmount,
        int discountAmount,
        int finalAmount,
        CouponInfo coupon,
        TossPaymentRequest tossPaymentRequest
) {
    public record CouponInfo(String name, String couponCode) {}
    public record TossPaymentRequest(String orderId, int amount, String customerKey) {}
}