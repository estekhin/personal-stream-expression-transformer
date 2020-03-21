package com.github.estekhin.set;

import org.jetbrains.annotations.NotNull;

public final class ExpressionTypeException extends RuntimeException {

    public ExpressionTypeException(@NotNull String message) {
        super(message);
    }

}
