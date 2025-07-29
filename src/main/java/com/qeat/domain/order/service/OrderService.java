package com.qeat.domain.order.service;

import com.qeat.domain.coupon.entity.UserCoupon;
import com.qeat.domain.coupon.repository.UserCouponRepository;
import com.qeat.domain.order.dto.request.OrderConfirmRequest;
import com.qeat.domain.order.dto.request.OrderRequest;
import com.qeat.domain.order.dto.response.OrderConfirmResponse;
import com.qeat.domain.order.dto.response.OrderResponse;
import com.qeat.domain.order.dto.response.OrderStatusResponse;
import com.qeat.domain.order.entity.CartItem;
import com.qeat.domain.order.entity.Order;
import com.qeat.domain.order.entity.OrderItem;
import com.qeat.domain.order.exception.code.OrderErrorCode;
import com.qeat.domain.order.repository.CartItemRepository;
import com.qeat.domain.order.repository.OrderItemRepository;
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
    private final OrderItemRepository orderItemRepository;

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

    public OrderConfirmResponse confirmOrder(OrderConfirmRequest request) {
        Long userId = extractUserId();

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));

        if (!order.getUserId().equals(userId)) {
            throw new CustomException(OrderErrorCode.UNAUTHORIZED);
        }

        if (order.getFinalAmount() != request.getAmount()) {
            throw new CustomException(OrderErrorCode.PAYMENT_AMOUNT_MISMATCH);
        }

        // TODO: Toss 결제 실제 연동이 필요할 경우 여기서 검증 요청

        // 쿠폰 사용 처리
        if (order.getUserCouponId() != null) {
            userCouponRepository.findById(order.getUserCouponId())
                    .ifPresent(userCoupon -> {
                        userCoupon.setIsUsed(true);
                        userCouponRepository.save(userCoupon);
                    });
        }

        return OrderConfirmResponse.builder()
                .orderId(order.getId())
                .finalAmount(order.getFinalAmount())
                .build();
    }

    public OrderStatusResponse getOrderStatus(Long orderId) {
        Long userId = extractUserId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));

        if (!order.getUserId().equals(userId)) {
            throw new CustomException(OrderErrorCode.UNAUTHORIZED);
        }

        // 주문 메뉴 목록 조회
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

        List<OrderStatusResponse.OrderMenuItem> items = orderItems.stream()
                .map(item -> new OrderStatusResponse.OrderMenuItem(
                        item.getMenu().getId(),
                        item.getMenu().getName(),
                        item.getQuantity()
                )).toList();

        return OrderStatusResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus().name()) // "주문확인", "조리중", "조리완료"
                .items(items)
                .finalAmount(order.getFinalAmount())
                .build();
    }
}