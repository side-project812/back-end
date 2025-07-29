package com.qeat.domain.order.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long storeId;

    @Column(name = "user_coupon_id", nullable = true)
    private Long userCouponId;

    private int originalAmount;

    private int discountAmount;

    private int finalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}