package com.github.estekhin.set.ast;

import com.github.estekhin.set.ExpressionTypeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FilterCallNode extends CallNode {

    public static final @NotNull String FILTER_START = "filter{";
    public static final @NotNull String FILTER_END = "}";


    public FilterCallNode(@NotNull ExpressionNode operand) {
        super(operand);
        if (operand.type() != ExpressionType.BOOLEAN) {
            throw new ExpressionTypeException(String.format(
                    "filter operand '%s' has invalid type %s",
                    operand, operand.type()
            ));
        }
    }


    @Override
    public <R> @Nullable R visit(@NotNull NodeVisitor<R> visitor) {
        return visitor.visitFilterCallNode(this);
    }


    @Override
    public @NotNull String toString() {
        return FILTER_START + operand + FILTER_END;
    }

}
