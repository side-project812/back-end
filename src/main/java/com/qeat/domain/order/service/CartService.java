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
import com.qeat.global.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public void addCartItems(CartAddRequest request) {
        Long userId = extractUserId();

        for (CartAddRequest.CartItem item : request.items()) {
            Menu menu = menuRepository.findById(item.menuId())
                    .orElseThrow(() -> new CustomException(OrderErrorCode.STORE_NOT_FOUND));

            // 기존에 같은 메뉴가 있는지 확인
            Optional<CartItem> optionalItem = cartItemRepository.findByUserIdAndStoreIdAndMenuId(
                    userId,
                    request.storeId(),
                    item.menuId()
            );

            if (optionalItem.isPresent()) {
                CartItem existingItem = optionalItem.get();
                existingItem.addQuantity(item.quantity());
                // JPA에서는 트랜잭션 내에서 엔티티 변경 시 save 호출 없이도 자동 감지됨 (Dirty Checking)
            } else {
                CartItem newItem = CartItem.builder()
                        .userId(userId)
                        .storeId(request.storeId())
                        .menu(menu)
                        .quantity(item.quantity())
                        .build();
                cartItemRepository.save(newItem);
            }
        }
    }

    private Long extractUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new CustomException(OrderErrorCode.UNAUTHORIZED);
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUserId();
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
