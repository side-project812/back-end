package com.qeat.domain.store.exception.code;

import com.qeat.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StoreErrorCode implements BaseErrorCode {

    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE_001", "가게 정보를 찾을 수 없습니다."),
    STORE_ALREADY_BOOKMARKED(HttpStatus.CONFLICT, "STORE_002", "이미 저장된 가게입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    // ✅ BaseErrorCode 메서드 구현
    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}