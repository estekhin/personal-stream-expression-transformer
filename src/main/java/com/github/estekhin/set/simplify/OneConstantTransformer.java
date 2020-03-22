package com.github.estekhin.set.simplify;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.NumberNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class OneConstantTransformer extends BinaryOperationWithNumberTransformer {

    @Override
    protected @Nullable ExpressionNode tryApplyToNumberOpAny(@NotNull NumberNode number, @NotNull BinaryOperation operation, @NotNull ExpressionNode otherOperand) {
        if (number.getValue() != 1) {
            return null;
        }
        if (operation == BinaryOperation.MULTIPLY) {
            return otherOperand;
        }
        return null;
    }

    @Override
    protected @Nullable ExpressionNode tryApplyToAnyOpNumber(@NotNull ExpressionNode otherOperand, @NotNull BinaryOperation operation, @NotNull NumberNode number) {
        if (number.getValue() != 1) {
            return null;
        }
        if (operation == BinaryOperation.MULTIPLY) {
            return otherOperand;
        }
        return null;
    }

}
