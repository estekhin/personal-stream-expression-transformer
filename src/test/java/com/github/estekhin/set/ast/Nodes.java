package com.github.estekhin.set.ast;

import org.jetbrains.annotations.NotNull;

public final class Nodes {

    public static @NotNull StreamExpressionNode expression(@NotNull CallNode... calls) {
        return new StreamExpressionNode(calls);
    }


    public static @NotNull MapCallNode map(@NotNull ExpressionNode operand) {
        return new MapCallNode(operand);
    }

    public static @NotNull FilterCallNode filter(@NotNull ExpressionNode operand) {
        return new FilterCallNode(operand);
    }


    public static @NotNull NumberNode number(long value) {
        return new NumberNode(value);
    }

    public static @NotNull BooleanNode bool(boolean value) {
        return new BooleanNode(value);
    }

    public static @NotNull ElementNode element() {
        return new ElementNode();
    }

    public static @NotNull BinaryOperationNode op(@NotNull ExpressionNode operand1, @NotNull BinaryOperation operation, @NotNull ExpressionNode operand2) {
        return new BinaryOperationNode(operand1, operation, operand2);
    }


    private Nodes() {
    }

}
