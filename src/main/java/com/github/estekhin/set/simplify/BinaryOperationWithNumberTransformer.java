package com.github.estekhin.set.simplify;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.NumberNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class BinaryOperationWithNumberTransformer implements BinaryOperationTransformer {

    @Override
    public final @Nullable ExpressionNode tryApply(@NotNull ExpressionNode operand1, @NotNull BinaryOperation operation, @NotNull ExpressionNode operand2) {
        if (operand1 instanceof NumberNode) {
            return tryApplyToNumberOpAny((NumberNode) operand1, operation, operand2);
        } else if (operand2 instanceof NumberNode) {
            return tryApplyToAnyOpNumber(operand1, operation, (NumberNode) operand2);
        }
        return null;
    }

    protected abstract @Nullable ExpressionNode tryApplyToNumberOpAny(@NotNull NumberNode number, @NotNull BinaryOperation operation, @NotNull ExpressionNode otherOperand);

    protected abstract @Nullable ExpressionNode tryApplyToAnyOpNumber(@NotNull ExpressionNode otherOperand, @NotNull BinaryOperation operation, @NotNull NumberNode number);

}
