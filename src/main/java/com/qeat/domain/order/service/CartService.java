package com.qeat.domain.order.service;

import com.qeat.domain.order.dto.request.CartAddRequest;
import com.qeat.domain.order.dto.response.CartItemResponse;
import com.qeat.domain.order.dto.response.CartResponse;
import com.qeat.domain.order.entity.CartItem;
import com.qeat.domain.order.exception.code.OrderErrorCode;
import com.qeat.domain.order.repository.CartItemRepository;
import com.qeat.domain.store.entity.Menu;
import com.qeat.domain.store.repository.MenuRepository;
import com.qeat.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final MenuRepository menuRepository;

    public void addCartItems(CartAddRequest request) {
        Long userId = extractUserId();

        cartItemRepository.deleteByUserId(userId); // 기존 장바구니 비우기

        for (CartAddRequest.CartItem item : request.items()) {
            Menu menu = menuRepository.findById(item.menuId())
                    .orElseThrow(() -> new CustomException(OrderErrorCode.STORE_NOT_FOUND));

            CartItem entity = CartItem.builder()
                    .userId(userId)
                    .storeId(request.storeId())
                    .menu(menu)
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

    public CartResponse getCartItems(Long storeId) {
        Long userId = extractUserId();

        List<CartItem> items = cartItemRepository.findByUserId(userId).stream()
                .filter(item -> item.getStoreId().equals(storeId))
                .toList();

        List<CartItemResponse> itemResponses = items.stream().map(item ->
                new CartItemResponse(
                        item.getMenu().getId(),
                        item.getMenu().getName(),
                        item.getQuantity(),
                        item.getMenu().getPrice()
                )
        ).toList();

        int totalAmount = itemResponses.stream()
                .mapToInt(item -> item.price() * item.quantity())
                .sum();

        return new CartResponse(itemResponses, totalAmount);
    }
}
