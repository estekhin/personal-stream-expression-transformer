package com.github.estekhin.set.simplify;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.ExpressionType;
import com.github.estekhin.set.ast.NumberNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class SimpleConstantFoldBinaryOperationTransformer implements BinaryOperationTransformer {

    @Override
    public @Nullable ExpressionNode tryApply(@NotNull ExpressionNode operand1, @NotNull BinaryOperation operation, @NotNull ExpressionNode operand2) {
        if (operation.getResultType() == ExpressionType.INTEGER
                && operand1 instanceof NumberNode
                && operand2 instanceof NumberNode
        ) {
            switch (operation) {
                case ADD:
                    // <number1> + <number2> => <number1 + number2>
                    return new NumberNode(Math.addExact(((NumberNode) operand1).getValue(), ((NumberNode) operand2).getValue()));
                case SUBTRACT:
                    // <number1> - <number2> => <number1 - number2>
                    return new NumberNode(Math.subtractExact(((NumberNode) operand1).getValue(), ((NumberNode) operand2).getValue()));
                case MULTIPLY:
                    // <number1> * <number2> => <number1 * number2>
                    return new NumberNode(Math.multiplyExact(((NumberNode) operand1).getValue(), ((NumberNode) operand2).getValue()));
                default:
                    throw new IllegalArgumentException("unexpected BinaryOperation: " + operation);
            }
        }
        return null;
    }

}
