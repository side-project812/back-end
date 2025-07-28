package com.qeat.domain.order.dto.response;

import java.util.List;

public record CartResponse(
        List<CartItemResponse> items,
        int totalAmount
) {}