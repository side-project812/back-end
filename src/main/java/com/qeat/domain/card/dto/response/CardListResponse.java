package com.qeat.domain.card.dto.response;

import lombok.Builder;

@Builder
public class CardListResponse {
    private Long cardId;
    private String cardType;
    private String last4;
    private boolean isDefault;
}