package com.github.estekhin.set;

import java.util.Objects;

import com.github.estekhin.set.ast.StreamExpressionNode;
import org.jetbrains.annotations.NotNull;

public final class StreamExpression {

    public static @NotNull StreamExpressionNode parse(@NotNull String source) {
        return new StreamExpressionParser(source).parse();
    }

    public static @NotNull String format(@NotNull StreamExpressionNode node) {
        return node.toString();
    }


    public static @NotNull StreamExpressionNode transform(@NotNull StreamExpressionNode node) {
        return Objects.requireNonNull(node.visit(new FilterMapTransformer()));
    }

    public static @NotNull StreamExpressionNode simplify(@NotNull StreamExpressionNode node) {
        return Objects.requireNonNull(node.visit(new SimplifyTransformer()));
    }


    private StreamExpression() {
    }

}