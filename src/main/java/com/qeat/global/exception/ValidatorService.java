package com.qeat.global.exception;

import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public abstract class ValidatorService {

    protected <T> void nonNull(T input, String message) {
        if (Objects.isNull(input)) {
            throw new UnsatisfyingParameterException(message);
        }
    }

    protected void nonBlank(String input, String message) {
        nonNull(input, message);
        if (input.isBlank()) {
            throw new UnsatisfyingParameterException(message);
        }
    }

    protected void validate(BooleanSupplier test, String message) {
        if (test.getAsBoolean()) {
            throw new UnsatisfyingParameterException(message);
        }
    }

    protected <T> void nestedNonNull(T input, String message, Consumer<T> function) {
        nonNull(input, message);
        function.accept(input);
    }

    protected <T> void elementNonNull(List<T> list) {
        list.forEach(element -> nonNull(element, "비어 있는 항목이 있습니다."));
    }

    protected <T> void empty(List<T> list, String message) {
        if (list.isEmpty()) {
            throw new UnsatisfyingParameterException(message);
        }
    }
}
