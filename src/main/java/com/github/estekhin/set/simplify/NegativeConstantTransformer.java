package com.github.estekhin.set.simplify;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.BinaryOperationNode;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.Nodes;
import com.github.estekhin.set.ast.NumberNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class NegativeConstantTransformer extends BinaryOperationWithNumberTransformer {

    @Override
    protected @Nullable ExpressionNode tryApplyToNumberOpAny(@NotNull NumberNode number, @NotNull BinaryOperation operation, @NotNull ExpressionNode otherOperand) {
        if (number.getValue() >= 0) {
            return null;
        }
        if (operation == BinaryOperation.ADD) {
            return Nodes.op(
                    otherOperand,
                    BinaryOperation.SUBTRACT,
                    Nodes.number(Math.subtractExact(0L, number.getValue()))
            );
        }
        if (operation == BinaryOperation.EQUALS || operation == BinaryOperation.GREATER_THAN || operation == BinaryOperation.LESS_THAN) {
            if (otherOperand instanceof BinaryOperationNode) {
                BinaryOperationNode binary = (BinaryOperationNode) otherOperand;
                if (binary.getOperation() == BinaryOperation.SUBTRACT) {
                    return Nodes.op(
                            Nodes.op(
                                    binary.getOperand2(),
                                    binary.getOperation(),
                                    binary.getOperand1()
                            ),
                            operation,
                            Nodes.number(Math.subtractExact(0L, number.getValue()))
                    );
                }
            }
        }
        return null;
    }

    @Override
    protected @Nullable ExpressionNode tryApplyToAnyOpNumber(@NotNull ExpressionNode otherOperand, @NotNull BinaryOperation operation, @NotNull NumberNode number) {
        if (number.getValue() >= 0) {
            return null;
        }
        if (operation == BinaryOperation.ADD) {
            return Nodes.op(
                    otherOperand,
                    BinaryOperation.SUBTRACT,
                    Nodes.number(Math.subtractExact(0L, number.getValue()))
            );
        }
        if (operation == BinaryOperation.SUBTRACT) {
            return Nodes.op(
                    otherOperand,
                    BinaryOperation.ADD,
                    Nodes.number(Math.subtractExact(0L, number.getValue()))
            );
        }
        if (operation == BinaryOperation.EQUALS || operation == BinaryOperation.GREATER_THAN || operation == BinaryOperation.LESS_THAN) {
            if (otherOperand instanceof BinaryOperationNode) {
                BinaryOperationNode binary = (BinaryOperationNode) otherOperand;
                if (binary.getOperation() == BinaryOperation.SUBTRACT) {
                    return Nodes.op(
                            Nodes.number(Math.subtractExact(0L, number.getValue())),
                            operation,
                            Nodes.op(
                                    binary.getOperand2(),
                                    binary.getOperation(),
                                    binary.getOperand1()
                            )
                    );
                }
            }
        }
        return null;
    }

}
