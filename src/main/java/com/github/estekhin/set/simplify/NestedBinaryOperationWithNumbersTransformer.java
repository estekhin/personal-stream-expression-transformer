package com.github.estekhin.set.simplify;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.BinaryOperationNode;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.NumberNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class NestedBinaryOperationWithNumbersTransformer extends BinaryOperationWithNumberTransformer {

    @Override
    protected final @Nullable ExpressionNode tryApplyToNumberOpAny(@NotNull NumberNode number, @NotNull BinaryOperation operation, @NotNull ExpressionNode otherOperand) {
        if (otherOperand instanceof BinaryOperationNode) {
            BinaryOperationNode binary = (BinaryOperationNode) otherOperand;
            if (binary.getOperand1() instanceof NumberNode) {
                return tryApplyToNumberOp2NumberOp1Any(
                        number, operation,
                        (NumberNode) binary.getOperand1(), binary.getOperation(), binary.getOperand2()
                );
            } else if (binary.getOperand2() instanceof NumberNode) {
                return tryApplyToNumberOp2AnyOp1Number(
                        number, operation,
                        binary.getOperand1(), binary.getOperation(), (NumberNode) binary.getOperand2()
                );
            }
        }
        return null;
    }

    @Override
    protected final @Nullable ExpressionNode tryApplyToAnyOpNumber(@NotNull ExpressionNode otherOperand, @NotNull BinaryOperation operation, @NotNull NumberNode number) {
        if (otherOperand instanceof BinaryOperationNode) {
            BinaryOperationNode binary = (BinaryOperationNode) otherOperand;
            if (binary.getOperand1() instanceof NumberNode) {
                return tryApplyToNumberOp1AnyOp2Number(
                        (NumberNode) binary.getOperand1(), binary.getOperation(), binary.getOperand2(),
                        operation, number
                );
            } else if (binary.getOperand2() instanceof NumberNode) {
                return tryApplyToAnyOp1NumberOp2Number(
                        binary.getOperand1(), binary.getOperation(), (NumberNode) binary.getOperand2(),
                        operation, number
                );
            }
        }
        return null;
    }


    protected abstract @Nullable ExpressionNode tryApplyToNumberOp2NumberOp1Any(
            @NotNull NumberNode number2, @NotNull BinaryOperation operation2,
            @NotNull NumberNode number1, @NotNull BinaryOperation operation1, @NotNull ExpressionNode otherOperand
    );

    protected abstract @Nullable ExpressionNode tryApplyToNumberOp2AnyOp1Number(
            @NotNull NumberNode number2, @NotNull BinaryOperation operation2,
            @NotNull ExpressionNode otherOperand, @NotNull BinaryOperation operation1, @NotNull NumberNode number1
    );

    protected abstract @Nullable ExpressionNode tryApplyToAnyOp1NumberOp2Number(
            @NotNull ExpressionNode otherOperand, @NotNull BinaryOperation operation1, @NotNull NumberNode number1,
            @NotNull BinaryOperation operation2, @NotNull NumberNode number2
    );

    protected abstract @Nullable ExpressionNode tryApplyToNumberOp1AnyOp2Number(
            @NotNull NumberNode number1, @NotNull BinaryOperation operation1, @NotNull ExpressionNode otherOperand,
            @NotNull BinaryOperation operation2, @NotNull NumberNode number2
    );

}
