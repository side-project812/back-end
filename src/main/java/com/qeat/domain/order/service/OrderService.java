package com.qeat.domain.order.service;

import com.qeat.domain.coupon.entity.UserCoupon;
import com.qeat.domain.coupon.repository.UserCouponRepository;
import com.qeat.domain.order.dto.request.OrderRequest;
import com.qeat.domain.order.dto.response.OrderResponse;
import com.qeat.domain.order.entity.CartItem;
import com.qeat.domain.order.entity.Order;
import com.qeat.domain.order.exception.code.OrderErrorCode;
import com.qeat.domain.order.repository.CartItemRepository;
import com.qeat.domain.order.repository.OrderRepository;
import com.qeat.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final UserCouponRepository userCouponRepository;

    public OrderResponse createOrder(OrderRequest request) {
        Long userId = extractUserId();

        List<CartItem> cartItems = cartItemRepository.findByUserId(userId).stream()
                .filter(item -> item.getStoreId().equals(request.getStoreId()))
                .toList();

        int originalAmount = cartItems.stream()
                .mapToInt(item -> item.getMenu().getPrice() * item.getQuantity())
                .sum();

        int discountAmount = 0;
        Long userCouponId = null;
        OrderResponse.CouponInfo couponInfo = null;

        if (request.getCouponCode() != null) {
            UserCoupon userCoupon = userCouponRepository
                    .findByUserIdAndCoupon_CouponCodeAndIsUsedFalse(userId, request.getCouponCode())
                    .orElseThrow(() -> new CustomException(OrderErrorCode.COUPON_NOT_FOUND));

            discountAmount = userCoupon.getCoupon().getDiscountAmount();
            userCouponId = userCoupon.getId();
            couponInfo = new OrderResponse.CouponInfo(
                    userCoupon.getCoupon().getName(),
                    userCoupon.getCoupon().getCouponCode()
            );
        }

        int finalAmount = originalAmount - discountAmount;

        Order order = orderRepository.save(Order.builder()
                .userId(userId)
                .storeId(request.getStoreId())
                .userCouponId(userCouponId)
                .originalAmount(originalAmount)
                .discountAmount(discountAmount)
                .finalAmount(finalAmount)
                .build()
        );

        OrderResponse.TossPaymentRequest toss = new OrderResponse.TossPaymentRequest(
                "order_" + order.getId(),
                finalAmount,
                "user_" + userId
        );

        return OrderResponse.builder()
                .orderId(order.getId())
                .originalAmount(originalAmount)
                .discountAmount(discountAmount)
                .finalAmount(finalAmount)
                .coupon(couponInfo)
                .tossPaymentRequest(toss)
                .build();
    }

    private Long extractUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new CustomException(OrderErrorCode.UNAUTHORIZED);
        }
        return (Long) auth.getPrincipal();
    }
}