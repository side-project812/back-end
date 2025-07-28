package com.qeat.domain.store.exception.handler;

import com.qeat.domain.store.exception.code.StoreErrorCode;
import com.qeat.global.apiPayload.CustomResponse;
import com.qeat.global.apiPayload.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
// @RestControllerAdvice(basePackages = "com.qeat.domain.store")
public class StoreExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomResponse<?>> handleCustomException(CustomException e, HttpServletRequest request) {
        if (!(e.getCode() instanceof StoreErrorCode)) {
            // 다른 도메인 에러는 패스
            return null;
        }

        StoreErrorCode errorCode = (StoreErrorCode) e.getCode();
        log.warn("StoreExceptionHandler: {} - {}", errorCode.getCode(), errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(CustomResponse.onFailure(errorCode.getCode(), errorCode.getMessage()));
    }
}