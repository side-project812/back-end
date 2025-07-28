package com.qeat.domain.order.dto.response;

public record CartItemResponse(
        Long menuId,
        String name,
        int quantity,
        int price
) {}