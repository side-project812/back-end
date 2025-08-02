package com.qeat.domain.card.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CardRegisterResponse {
    private Long cardId;
    private String billingKey;
    private String cardType;
    private String last4;
    private boolean isDefault;
}