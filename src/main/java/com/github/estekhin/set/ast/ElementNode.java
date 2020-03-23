package com.github.estekhin.set.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ElementNode extends ExpressionNode {

    static final @NotNull String ELEMENT = "element";


    ElementNode() {
    }


    @Override
    public @NotNull ExpressionType type() {
        return ExpressionType.INTEGER;
    }

    @Override
    public <R> @Nullable R visit(@NotNull NodeVisitor<R> visitor) {
        return visitor.visitElementNode(this);
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
