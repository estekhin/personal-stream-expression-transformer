package com.github.estekhin.set.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BooleanNode extends ExpressionNode {

    public static final @NotNull String TRUE = "(1=1)";
    public static final @NotNull String FALSE = "(1=0)";

    private final boolean value;


    public BooleanNode(boolean value) {
        this.value = value;
    }


    public boolean isTrue() {
        return value;
    }

    @Override
    public @NotNull ExpressionType type() {
        return ExpressionType.BOOLEAN;
    }

    @Override
    public <R> @Nullable R visit(@NotNull NodeVisitor<R> visitor) {
        return visitor.visitBooleanNode(this);
    }


    @Override
    public int hashCode() {
        return Boolean.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        BooleanNode other = (BooleanNode) obj;
        return value == other.value;
    }

    @Override
    public @NotNull String toString() {
        return value ? TRUE : FALSE;
    }

}
