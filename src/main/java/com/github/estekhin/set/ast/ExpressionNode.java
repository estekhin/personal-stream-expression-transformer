package com.github.estekhin.set.ast;

import org.jetbrains.annotations.NotNull;

public abstract class ExpressionNode extends Node {

    protected ExpressionNode() {
    }


    public abstract @NotNull ExpressionType type();

}
