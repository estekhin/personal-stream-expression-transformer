package com.github.estekhin.set.simplify;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.BinaryOperationNode;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.NumberNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class SimpleConstantFoldTransformer implements BinaryOperationTransformer {

    @Override
    public @Nullable ExpressionNode tryApply(@NotNull ExpressionNode operand1, @NotNull BinaryOperation operation, @NotNull ExpressionNode operand2) {
        if (operand1 instanceof NumberNode && operand2 instanceof NumberNode) {
            return tryApplyToNumberOpNumber((NumberNode) operand1, operation, (NumberNode) operand2);
        }
        return null;
    }

    private @Nullable ExpressionNode tryApplyToNumberOpNumber(@NotNull NumberNode operand1, @NotNull BinaryOperation operation, @NotNull NumberNode operand2) {
        switch (operation) {
            case ADD:
                return new NumberNode(Math.addExact(operand1.getValue(), operand2.getValue()));
            case SUBTRACT:
                return new NumberNode(Math.subtractExact(operand1.getValue(), operand2.getValue()));
            case MULTIPLY:
                return new NumberNode(Math.multiplyExact(operand1.getValue(), operand2.getValue()));
            case EQUALS:
                return isBooleanConstant(operand1, operand2)
                        ? null
                        : booleanConstant(operand1.getValue() == operand2.getValue());
            case GREATER_THAN:
                return booleanConstant(operand1.getValue() > operand2.getValue());
            case LESS_THAN:
                return booleanConstant(operand1.getValue() < operand2.getValue());
            default:
                return null;
        }
    }

    private boolean isBooleanConstant(@NotNull NumberNode operand1, @NotNull NumberNode operand2) {
        return operand1.getValue() == 1 && (operand2.getValue() == 0 || operand2.getValue() == 1);
    }

    private @NotNull ExpressionNode booleanConstant(boolean value) {
        return new BinaryOperationNode(
                new NumberNode(1),
                BinaryOperation.EQUALS,
                new NumberNode(value ? 1 : 0)
        );
    }

}
