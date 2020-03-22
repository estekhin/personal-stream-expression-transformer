package com.github.estekhin.set.ast;

import org.jetbrains.annotations.NotNull;

public final class ElementNode extends ExpressionNode {

    public static final @NotNull String ELEMENT = "element";


    @Override
    public @NotNull ExpressionType type() {
        return ExpressionType.INTEGER;
    }

    @Override
    public @NotNull ExpressionNode replaceElement(@NotNull ExpressionNode replacement) {
        return replacement;
    }


    @Override
    public @NotNull String toString() {
        return ELEMENT;
    }

}
