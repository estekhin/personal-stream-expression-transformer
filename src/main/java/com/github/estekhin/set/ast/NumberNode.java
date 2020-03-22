package com.github.estekhin.set.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class NumberNode extends ExpressionNode {

    public static final @NotNull String UNARY_MINUS = "-";

    private final long value;


    public NumberNode(long value) {
        this.value = value;
    }


    public long getValue() {
        return value;
    }

    @Override
    public @NotNull ExpressionType type() {
        return ExpressionType.INTEGER;
    }

    @Override
    public <R> @Nullable R visit(@NotNull NodeVisitor<R> visitor) {
        return visitor.visitNumberNode(this);
    }


    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        NumberNode other = (NumberNode) obj;
        return value == other.value;
    }

    @Override
    public @NotNull String toString() {
        return String.valueOf(value);
    }

}
