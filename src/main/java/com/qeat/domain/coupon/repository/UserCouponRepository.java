package com.qeat.domain.coupon.repository;

import com.qeat.domain.coupon.entity.Coupon;
import com.qeat.domain.coupon.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    Optional<UserCoupon> findByUserIdAndCoupon_CouponCodeAndIsUsedFalse(Long userId, String couponCode);
    boolean existsByUserIdAndCoupon(Long userId, Coupon coupon);
}