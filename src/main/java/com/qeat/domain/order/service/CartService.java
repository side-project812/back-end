package com.qeat.domain.order.service;

import com.qeat.domain.order.dto.request.CartAddRequest;
import com.qeat.domain.order.entity.CartItem;
import com.qeat.domain.order.exception.code.OrderErrorCode;
import com.qeat.domain.order.repository.CartItemRepository;
import com.qeat.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;

    public void addCartItems(CartAddRequest request) {
        Long userId = extractUserId();

        cartItemRepository.deleteByUserId(userId); // 기존 장바구니 비우기

        for (CartAddRequest.CartItem item : request.items()) {
            CartItem entity = CartItem.builder()
                    .userId(userId)
                    .storeId(request.storeId())
                    .menuId(item.menuId())
                    .quantity(item.quantity())
                    .build();
            cartItemRepository.save(entity);
        }
    }

    private Long extractUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new CustomException(OrderErrorCode.UNAUTHORIZED);
        }
        return (Long) authentication.getPrincipal();
    }
}
