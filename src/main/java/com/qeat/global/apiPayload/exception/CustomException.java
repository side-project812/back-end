package com.qeat.global.apiPayload.exception;

import com.qeat.global.apiPayload.code.BaseErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final BaseErrorCode code;

    public CustomException(BaseErrorCode errorCode) {
        this.code = errorCode;
    }

    public BaseErrorCode getErrorCode() {
        return this.code;
    }
}