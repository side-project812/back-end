package com.qeat.domain.user.exception.handler;

import com.qeat.global.apiPayload.code.BaseErrorCode;
import com.qeat.global.apiPayload.exception.CustomException;

public class UserException extends CustomException {
    public UserException(BaseErrorCode code) {
        super(code);
    }
}
