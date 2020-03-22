package com.github.estekhin.set.ast;

import com.github.estekhin.set.ExpressionTypeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MapCallNode extends CallNode {

    public static final @NotNull String MAP_START = "map{";
    public static final @NotNull String MAP_END = "}";


    public MapCallNode(@NotNull ExpressionNode operand) {
        super(operand);
        if (operand.type() != ExpressionType.INTEGER) {
            throw new ExpressionTypeException(String.format(
                    "map operand '%s' has invalid type %s",
                    operand, operand.type()
            ));
        }
    }


    @Override
    public <R> @Nullable R visit(@NotNull NodeVisitor<R> visitor) {
        return visitor.visitMapCallNode(this);
    }


    @Override
    public @NotNull String toString() {
        return MAP_START + operand + MAP_END;
    }

}
