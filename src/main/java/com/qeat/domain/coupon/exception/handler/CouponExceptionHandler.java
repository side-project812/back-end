package com.qeat.domain.coupon.exception.handler;

import com.qeat.domain.coupon.exception.code.CouponErrorCode;
import com.qeat.global.apiPayload.CustomResponse;
import com.qeat.global.apiPayload.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
// @RestControllerAdvice(basePackages = "com.qeat.domain.coupon")
public class CouponExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomResponse<?>> handleCustomException(CustomException e, HttpServletRequest request) {
        if (!(e.getCode() instanceof CouponErrorCode)) {
            return null; // 다른 도메인 에러는 여기서 처리하지 않음
        }

        CouponErrorCode errorCode = (CouponErrorCode) e.getCode();
        log.warn("CouponExceptionHandler: {} - {}", errorCode.getCode(), errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(CustomResponse.onFailure(errorCode.getCode(), errorCode.getMessage()));
    }
}