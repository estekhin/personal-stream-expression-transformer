package com.github.estekhin.set.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface NodeVisitor<R> {

    default @Nullable R visitStreamExpressionNode(@NotNull StreamExpressionNode node) {
        return null;
    }


    default @Nullable R visitFilterCallNode(@NotNull FilterCallNode node) {
        return null;
    }

    default @Nullable R visitMapCallNode(@NotNull MapCallNode node) {
        return null;
    }


    default @Nullable R visitNumberNode(@NotNull NumberNode node) {
        return null;
    }

    default @Nullable R visitElementNode(@NotNull ElementNode node) {
        return null;
    }

    default @Nullable R visitBinaryOperationNode(@NotNull BinaryOperationNode node) {
        return null;
    }

}
