package com.qeat.domain.coupon.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    private Boolean isUsed;

    public void setIsUsed(boolean used) {
        this.isUsed = used;
    }

    public void use() {
        this.isUsed = true;
    }

    public boolean isUsed() {
        return Boolean.TRUE.equals(this.isUsed);
    }
}