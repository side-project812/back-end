package com.qeat.domain.order.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record OrderStatusResponse(
        Long orderId,
        String status,
        List<OrderMenuItem> items,
        int finalAmount
) {
    public record OrderMenuItem(Long menuId, String name, int quantity) {}
}