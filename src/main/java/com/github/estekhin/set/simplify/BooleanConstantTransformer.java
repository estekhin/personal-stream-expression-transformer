package com.github.estekhin.set.simplify;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.BooleanNode;
import com.github.estekhin.set.ast.ExpressionNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class BooleanConstantTransformer implements BinaryOperationTransformer {

    @Override
    public @Nullable ExpressionNode tryApply(@NotNull ExpressionNode operand1, @NotNull BinaryOperation operation, @NotNull ExpressionNode operand2) {
        if (operand1 instanceof BooleanNode) {
            return tryApplyToBoolOpAny((BooleanNode) operand1, operation, operand2);
        } else if (operand2 instanceof BooleanNode) {
            return tryApplyToBoolOpAny((BooleanNode) operand2, operation, operand1);
        }
        return null;
    }

    private @Nullable ExpressionNode tryApplyToBoolOpAny(@NotNull BooleanNode booleanOperand, @NotNull BinaryOperation operation, @NotNull ExpressionNode otherOperand) {
        if (booleanOperand.isTrue()) {
            if (operation == BinaryOperation.AND) {
                return otherOperand;
            } else if (operation == BinaryOperation.OR) {
                return booleanOperand;
            }
        } else {
            if (operation == BinaryOperation.AND) {
                return booleanOperand;
            } else if (operation == BinaryOperation.OR) {
                return otherOperand;
            }
        }
        return null;
    }

}
