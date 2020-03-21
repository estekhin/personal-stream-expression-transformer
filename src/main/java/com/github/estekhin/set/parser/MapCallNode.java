package com.github.estekhin.set.parser;

import com.github.estekhin.set.ExpressionTypeException;
import org.jetbrains.annotations.NotNull;

public final class MapCallNode extends CallNode {

    MapCallNode(@NotNull ExpressionNode operand) {
        super(operand);
        if (operand.type() != ExpressionType.INTEGER) {
            throw new ExpressionTypeException(String.format(
                    "map operand '%s' has unexpected type %s",
                    operand, operand.type()
            ));
        }
    }


    @Override
    public @NotNull String toString() {
        return Tokens.MAP_START + operand + Tokens.MAP_END;
    }

}
