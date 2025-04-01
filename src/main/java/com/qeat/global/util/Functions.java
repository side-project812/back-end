package com.qeat.global.util;

import java.util.Objects;
import java.util.function.Consumer;

public class Functions {

    public static <T> void update(Consumer<T> function, T input) {
        if (Objects.nonNull(input)) {
            function.accept(input);
        }
    }

    public static void nonAction() {

    }

    public static <T> void nonAction(T unused) {

    }
}