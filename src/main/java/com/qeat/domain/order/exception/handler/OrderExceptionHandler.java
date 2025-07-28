package com.qeat.domain.order.exception.handler;

import com.qeat.domain.order.exception.code.OrderErrorCode;
import com.qeat.global.apiPayload.CustomResponse;
import com.qeat.global.apiPayload.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
// @RestControllerAdvice(basePackages = "com.qeat.domain.order")
public class OrderExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomResponse<?>> handleCustomException(CustomException e, HttpServletRequest request) {
        if (!(e.getCode() instanceof OrderErrorCode)) {
            return null; // 다른 도메인 예외는 무시
        }

        OrderErrorCode errorCode = (OrderErrorCode) e.getCode();
        log.warn("OrderException: {} - {}", errorCode.getCode(), errorCode.getMessage());

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(CustomResponse.onFailure(errorCode.getCode(), errorCode.getMessage()));
    }
}