package com.qeat.domain.coupon.service;

import com.qeat.domain.coupon.dto.request.CouponRegisterRequest;
import com.qeat.domain.coupon.dto.request.UseCouponRequest;
import com.qeat.domain.coupon.dto.response.CouponListResponse;
import com.qeat.domain.coupon.dto.response.CouponRegisterResponse;
import com.qeat.domain.coupon.dto.response.UseCouponResponse;
import com.qeat.domain.coupon.entity.Coupon;
import com.qeat.domain.coupon.entity.UserCoupon;
import com.qeat.domain.coupon.exception.code.CouponErrorCode;
import com.qeat.domain.coupon.repository.CouponRepository;
import com.qeat.domain.coupon.repository.UserCouponRepository;
import com.qeat.global.apiPayload.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    public CouponRegisterResponse registerCoupon(Long userId, CouponRegisterRequest request) {
        Coupon coupon = couponRepository.findByCouponCode(request.couponCode())
                .orElseThrow(() -> new CustomException(CouponErrorCode.INVALID_COUPON));

        // 이미 등록된 쿠폰인지 확인
        boolean alreadyRegistered = userCouponRepository.existsByUserIdAndCoupon(userId, coupon);
        if (alreadyRegistered) {
            throw new CustomException(CouponErrorCode.COUPON_ALREADY_REGISTERED); // 이미 등록된 쿠폰
        }

        // 등록
        UserCoupon userCoupon = UserCoupon.builder()
                .userId(userId)
                .coupon(coupon)
                .isUsed(false)
                .build();
        userCouponRepository.save(userCoupon);

        return CouponRegisterResponse.builder()
                .couponId(coupon.getId())
                .name(coupon.getName())
                .discountAmount(coupon.getDiscountAmount())
                .validFrom(coupon.getValidFrom().toString())
                .validTo(coupon.getValidTo().toString())
                .build();
    }

    @Transactional
    public UseCouponResponse useCoupon(Long userId, UseCouponRequest request) {
        Coupon coupon = couponRepository.findByCouponCode(request.couponCode())
                .orElseThrow(() -> new CustomException(CouponErrorCode.COUPON_NOT_FOUND));

        UserCoupon userCoupon = userCouponRepository.findByUserIdAndCouponId(userId, coupon.getId())
                .orElseThrow(() -> new CustomException(CouponErrorCode.COUPON_NOT_FOUND));

        if (userCoupon.isUsed()) {
            throw new CustomException(CouponErrorCode.ALREADY_USED);
        }

        userCoupon.use(); // used = true 로 변경
        return new UseCouponResponse(coupon.getId(), coupon.getDiscountAmount());
    }

    public List<CouponListResponse> getCouponList(Long userId) {
        List<UserCoupon> userCoupons = userCouponRepository.findByUserIdAndIsUsedFalse(userId);

        return userCoupons.stream()
                .map(userCoupon -> {
                    Coupon coupon = userCoupon.getCoupon();
                    return CouponListResponse.builder()
                            .couponId(coupon.getId())
                            .name(coupon.getName())
                            .discountAmount(coupon.getDiscountAmount())
                            .minOrderAmount(coupon.getMinOrderAmount())
                            .validFrom(coupon.getValidFrom().toString())
                            .validTo(coupon.getValidTo().toString())
                            .couponCode(coupon.getCouponCode())
                            .build();
                }).toList();
    }
}