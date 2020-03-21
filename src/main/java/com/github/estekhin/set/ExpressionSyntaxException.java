package com.github.estekhin.set;

import org.jetbrains.annotations.NotNull;

public final class ExpressionSyntaxException extends RuntimeException {

    ExpressionSyntaxException(@NotNull String message) {
        super(message);
    }

}
