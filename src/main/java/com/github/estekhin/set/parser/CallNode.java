package com.github.estekhin.set.parser;

import org.jetbrains.annotations.NotNull;

public abstract class CallNode extends Node {

    protected final @NotNull ExpressionNode operand;


    protected CallNode(@NotNull ExpressionNode operand) {
        this.operand = operand;
    }

}
