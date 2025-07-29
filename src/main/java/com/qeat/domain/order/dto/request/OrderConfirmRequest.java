package com.qeat.domain.order.dto.request;

import lombok.Getter;

@Getter
public class OrderConfirmRequest {
    private Long orderId;
    private String paymentKey;
    private int amount;
}