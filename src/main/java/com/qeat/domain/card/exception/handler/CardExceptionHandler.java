package com.qeat.domain.card.exception.handler;

import com.qeat.domain.card.exception.code.CardErrorCode;
import com.qeat.global.apiPayload.CustomResponse;
import com.qeat.global.apiPayload.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @RestControllerAdvice
public class CardExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomResponse<Void>> handleCustomException(CustomException e, HttpServletRequest request) {
        if (e.getErrorCode() instanceof CardErrorCode) {
            CardErrorCode code = (CardErrorCode) e.getErrorCode();
            return ResponseEntity
                    .status(code.getStatus())
                    .body(CustomResponse.onFailure(code.getCode(), code.getMessage()));
        }

        // 다른 예외로 던짐
        throw e;
    }
}