package com.github.estekhin.set;

import com.github.estekhin.set.parser.ExpressionParser;
import org.jetbrains.annotations.NotNull;

public class StreamExpressionTransformer {

    public @NotNull String transform(@NotNull String expression) {
        return new ExpressionParser(expression).parse().toString();
    }

}
