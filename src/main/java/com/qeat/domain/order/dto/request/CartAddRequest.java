package com.qeat.domain.order.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record CartAddRequest(
        Long storeId,
        List<CartItem> items
) {
    public record CartItem(Long menuId, int quantity) {}
}