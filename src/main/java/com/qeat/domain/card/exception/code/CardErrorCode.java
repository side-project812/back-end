package com.qeat.domain.card.exception.code;

import com.qeat.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CardErrorCode implements BaseErrorCode {

    CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "ERROR404_CARD_01", "해당 카드를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}