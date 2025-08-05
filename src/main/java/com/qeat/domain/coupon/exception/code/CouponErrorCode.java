package com.qeat.domain.coupon.exception.code;

import com.qeat.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CouponErrorCode implements BaseErrorCode {

    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "COUPON_001", "존재하지 않는 쿠폰 코드입니다."),
    COUPON_ALREADY_REGISTERED(HttpStatus.CONFLICT, "COUPON_002", "이미 등록된 쿠폰입니다."),
    COUPON_EXPIRED(HttpStatus.BAD_REQUEST, "COUPON_003", "만료된 쿠폰입니다."),
    INVALID_COUPON(HttpStatus.BAD_REQUEST, "COUPON_004", "유효하지 않은 쿠폰 코드입니다."),
    ALREADY_USED(HttpStatus.BAD_REQUEST, "COUPON_005", "이미 사용된 쿠폰입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}