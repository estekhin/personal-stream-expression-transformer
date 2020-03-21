package com.github.estekhin.set.parser;

import org.jetbrains.annotations.NotNull;

public final class ElementNode extends ExpressionNode {

    @Override
    public @NotNull ExpressionType type() {
        return ExpressionType.INTEGER;
    }

    @Override
    public @NotNull String toString() {
        return Tokens.ELEMENT;
    }

}
