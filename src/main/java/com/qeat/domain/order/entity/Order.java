package com.qeat.domain.order.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
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
    @Column(nullable = false)
    @Builder.Default // @Builder 사용 시 꼭 필요함
    private OrderStatus status = OrderStatus.주문확인;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
}