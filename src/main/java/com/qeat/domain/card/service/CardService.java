package com.qeat.domain.card.service;

import com.qeat.domain.card.dto.request.CardRegisterRequest;
import com.qeat.domain.card.dto.response.CardListResponse;
import com.qeat.domain.card.dto.response.CardRegisterResponse;
import com.qeat.domain.card.entity.Card;
import com.qeat.domain.card.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    public CardRegisterResponse registerCard(CardRegisterRequest request) {
        Long userId = extractUserIdFromSecurityContext();

        Card card = Card.builder()
                .userId(userId)
                .billingKey(request.billingKey())
                .cardType(request.cardType())
                .last4(request.last4())
                .isDefault(request.isDefault())
                .build();

        card = cardRepository.save(card);

        return CardRegisterResponse.builder()
                .cardId(card.getId())
                .billingKey(card.getBillingKey())
                .cardType(card.getCardType())
                .last4(card.getLast4())
                .isDefault(card.isDefault())
                .build();
    }

    private Long extractUserIdFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("사용자 인증 정보가 존재하지 않습니다.");
        }
        return (Long) authentication.getPrincipal();
    }

    public List<CardListResponse> getCardList() {
        Long userId = extractUserIdFromSecurityContext();

        return cardRepository.findByUserId(userId).stream()
                .map(card -> CardListResponse.builder()
                        .cardId(card.getId())
                        .cardType(card.getCardType())
                        .last4(card.getLast4())
                        .isDefault(card.isDefault())
                        .build())
                .collect(Collectors.toList());
    }
}