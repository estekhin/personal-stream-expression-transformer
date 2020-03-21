package com.github.estekhin.set;

import org.jetbrains.annotations.NotNull;

public class StreamExpressionTransformer {

    public @NotNull String transform(@NotNull String expression) {
        new ExpressionParser(expression).parseCallChain();
        return expression;
    }

}
