package com.github.estekhin.set.parser;

import org.jetbrains.annotations.NotNull;

public final class NumberNode extends ExpressionNode {

    private final long value;


    NumberNode(long value) {
        this.value = value;
    }


    @Override
    public @NotNull ExpressionType type() {
        return ExpressionType.INTEGER;
    }

    @Override
    public @NotNull String toString() {
        return String.valueOf(value);
    }

}
