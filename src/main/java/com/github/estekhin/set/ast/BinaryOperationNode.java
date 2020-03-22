package com.github.estekhin.set.ast;

import com.github.estekhin.set.ExpressionTypeException;
import org.jetbrains.annotations.NotNull;

public final class BinaryOperationNode extends ExpressionNode {

    public static final @NotNull String BINARY_EXPRESSION_START = "(";
    public static final @NotNull String BINARY_EXPRESSION_END = ")";

    private final @NotNull ExpressionNode operand1;
    private final @NotNull BinaryOperation operation;
    private final @NotNull ExpressionNode operand2;


    public BinaryOperationNode(@NotNull ExpressionNode operand1, @NotNull BinaryOperation operation, @NotNull ExpressionNode operand2) {
        if (operand1.type() != operation.getOperandsType()) {
            throw new ExpressionTypeException(String.format(
                    "%s operation operand '%s' has invalid type %s",
                    operation, operand1, operand1.type()
            ));
        }
        if (operand2.type() != operation.getOperandsType()) {
            throw new ExpressionTypeException(String.format(
                    "%s operation operand '%s' has invalid type %s",
                    operation, operand2, operand2.type()
            ));
        }
        this.operand1 = operand1;
        this.operation = operation;
        this.operand2 = operand2;
    }


    @Override
    public @NotNull ExpressionType type() {
        return operation.getResultType();
    }

    @Override
    public @NotNull ExpressionNode replaceElement(@NotNull ExpressionNode replacement) {
        return new BinaryOperationNode(
                operand1.replaceElement(replacement),
                operation,
                operand2.replaceElement(replacement)
        );
    }


    @Override
    public @NotNull String toString() {
        return BINARY_EXPRESSION_START + operand1 + operation + operand2 + BINARY_EXPRESSION_END;
    }

}
