package com.github.estekhin.set.ast;

import org.jetbrains.annotations.NotNull;

public final class ElementNode extends ExpressionNode {

    public static final @NotNull String ELEMENT = "element";


    @Override
    public @NotNull ExpressionType type() {
        return ExpressionType.INTEGER;
    }

    @Override
    public @NotNull ExpressionNode transform(@NotNull ExpressionNodeTransformer transformer) {
        return transformer.transformElementNode(this);
    }


    @Override
    public int hashCode() {
        return 0; // :facepalm:
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        return true;
    }

    @Override
    public @NotNull String toString() {
        return ELEMENT;
    }

}
