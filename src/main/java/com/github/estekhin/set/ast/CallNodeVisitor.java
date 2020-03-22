package com.github.estekhin.set.ast;

import org.jetbrains.annotations.NotNull;

public interface CallNodeVisitor {

    default void visitFilterCallNode(@NotNull FilterCallNode node) {
    }

    default void visitMapCallNode(@NotNull MapCallNode node) {
    }

}
