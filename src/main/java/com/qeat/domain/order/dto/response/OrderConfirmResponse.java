package com.qeat.domain.order.dto.response;

import lombok.Builder;

@Builder
public record OrderConfirmResponse(
        Long orderId,
        int finalAmount
) {}