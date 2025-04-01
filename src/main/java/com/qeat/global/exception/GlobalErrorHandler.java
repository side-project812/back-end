package com.qeat.global.exception;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import java.util.Arrays;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalErrorHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnsatisfyingParameterException.class)
    public ResponseEntity<ErrorResponse> exception(UnsatisfyingParameterException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(0, exception.getMessage()));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            UnrecognizedPropertyException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ErrorResponse> exception(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(0, "유효하지 않은 입력입니다."));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> allException(Exception exception) {
        log.error(Arrays.toString(exception.getStackTrace()));
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(-1, "서버에서 알 수 없는 이유로 문제가 발생했습니다."));
    }

    @Builder
    public record ErrorResponse(
            int code,
            String message
    ) {
    }

}
