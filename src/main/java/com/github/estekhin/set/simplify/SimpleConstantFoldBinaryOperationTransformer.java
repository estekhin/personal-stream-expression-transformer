package com.github.estekhin.set.simplify;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.ExpressionType;
import com.github.estekhin.set.ast.NumberNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class SimpleConstantFoldBinaryOperationTransformer implements BinaryOperationTransformer {
    /*
    <number1> + <number2> => <number1 + number2>
    <number1> - <number2> => <number1 - number2>
    <number1> * <number2> => <number1 * number2>
     */

    @Override
    public @Nullable ExpressionNode tryApply(@NotNull ExpressionNode operand1, @NotNull BinaryOperation operation, @NotNull ExpressionNode operand2) {
        if (operation.getResultType() == ExpressionType.INTEGER
                && operand1 instanceof NumberNode
                && operand2 instanceof NumberNode
        ) {
            return apply((NumberNode) operand1, operation, (NumberNode) operand2);
        } else {
            return null;
        }
    }

    @NotNull ExpressionNode apply(@NotNull NumberNode operand1, @NotNull BinaryOperation operation, @NotNull NumberNode operand2) {
        switch (operation) {
            case ADD:
                return new NumberNode(Math.addExact(operand1.getValue(), operand2.getValue()));
            case SUBTRACT:
                return new NumberNode(Math.subtractExact(operand1.getValue(), operand2.getValue()));
            case MULTIPLY:
                return new NumberNode(Math.multiplyExact(operand1.getValue(), operand2.getValue()));
            default:
                throw new IllegalArgumentException("unexpected BinaryOperation: " + operation);
        }
    }

}
