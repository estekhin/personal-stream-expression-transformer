package com.github.estekhin.set;

import java.util.Objects;

import com.github.estekhin.set.ast.StreamExpressionNode;
import org.jetbrains.annotations.NotNull;

public class StreamExpressionTransformer {

    public @NotNull String transform(@NotNull String expression) {
        return process(new StreamExpressionParser(expression).parse()).toString();
    }

    private @NotNull StreamExpressionNode process(@NotNull StreamExpressionNode streamExpression) {
        return Objects.requireNonNull(streamExpression.visit(new FilterMapTransformer()));
    }

}
