package com.qeat.domain.order.dto.request;

import lombok.Getter;

@Getter
public class OrderRequest {
    private Long storeId;
    private String couponCode; // optional
}