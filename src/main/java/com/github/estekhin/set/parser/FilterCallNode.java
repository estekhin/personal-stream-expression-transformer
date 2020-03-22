package com.github.estekhin.set.parser;

import com.github.estekhin.set.ExpressionTypeException;
import org.jetbrains.annotations.NotNull;

public final class FilterCallNode extends CallNode {

    public FilterCallNode(@NotNull ExpressionNode operand) {
        super(operand);
        if (operand.type() != ExpressionType.BOOLEAN) {
            throw new ExpressionTypeException(String.format(
                    "filter operand '%s' has unexpected type %s",
                    operand, operand.type()
            ));
        }
    }


    @Override
    public @NotNull String toString() {
        return Tokens.FILTER_START + operand + Tokens.FILTER_END;
    }

}
