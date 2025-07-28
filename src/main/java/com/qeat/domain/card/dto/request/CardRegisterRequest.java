package com.qeat.domain.card.dto.request;

public record CardRegisterRequest(
        String billingKey,
        String cardType,
        String last4,
        boolean isDefault
) {}
