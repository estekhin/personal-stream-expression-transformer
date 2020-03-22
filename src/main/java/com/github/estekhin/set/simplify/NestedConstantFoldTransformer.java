package com.github.estekhin.set.simplify;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.BinaryOperationNode;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.NumberNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class NestedConstantFoldTransformer extends NestedBinaryOperationWithNumbersTransformer {

    @Override
    protected @Nullable ExpressionNode tryApplyToNumberOp2NumberOp1Any(
            @NotNull NumberNode number2, @NotNull BinaryOperation operation2,
            @NotNull NumberNode number1, @NotNull BinaryOperation operation1, @NotNull ExpressionNode otherOperand
    ) {
        if (operation1 != BinaryOperation.ADD && operation1 != BinaryOperation.SUBTRACT) {
            return null;
        }
        if (operation2 == BinaryOperation.ADD) {
            return new BinaryOperationNode(
                    new NumberNode(Math.addExact(number2.getValue(), number1.getValue())),
                    operation1,
                    otherOperand
            );
        } else if (operation2 == BinaryOperation.SUBTRACT) {
            return new BinaryOperationNode(
                    new NumberNode(Math.subtractExact(number2.getValue(), number1.getValue())),
                    BinaryOperation.SUBTRACT,
                    otherOperand
            );
        } else if (operation2 == BinaryOperation.EQUALS || operation2 == BinaryOperation.GREATER_THAN || operation2 == BinaryOperation.LESS_THAN) {
            return new BinaryOperationNode(
                    new NumberNode(operation1 == BinaryOperation.ADD
                            ? Math.subtractExact(number2.getValue(), number1.getValue())
                            : Math.addExact(number2.getValue(), number1.getValue())
                    ),
                    operation2,
                    otherOperand
            );
        }
        return null;
    }

    @Override
    protected @Nullable ExpressionNode tryApplyToNumberOp2AnyOp1Number(
            @NotNull NumberNode number2, @NotNull BinaryOperation operation2,
            @NotNull ExpressionNode otherOperand, @NotNull BinaryOperation operation1, @NotNull NumberNode number1
    ) {
        if (operation1 != BinaryOperation.ADD && operation1 != BinaryOperation.SUBTRACT) {
            return null;
        }
        if (operation2 == BinaryOperation.ADD || operation2 == BinaryOperation.SUBTRACT) {
            return new BinaryOperationNode(
                    new NumberNode(operation1 == operation2
                            ? Math.addExact(number2.getValue(), number1.getValue())
                            : Math.subtractExact(number2.getValue(), number1.getValue())
                    ),
                    operation2,
                    otherOperand
            );
        } else if (operation2 == BinaryOperation.EQUALS || operation2 == BinaryOperation.GREATER_THAN || operation2 == BinaryOperation.LESS_THAN) {
            return new BinaryOperationNode(
                    new NumberNode(operation1 == BinaryOperation.ADD
                            ? Math.subtractExact(number2.getValue(), number1.getValue())
                            : Math.addExact(number2.getValue(), number1.getValue())
                    ),
                    operation2,
                    otherOperand
            );
        }
        return null;
    }

    @Override
    protected @Nullable ExpressionNode tryApplyToAnyOp1NumberOp2Number(
            @NotNull ExpressionNode otherOperand, @NotNull BinaryOperation operation1, @NotNull NumberNode number1,
            @NotNull BinaryOperation operation2, @NotNull NumberNode number2
    ) {
        if (operation1 != BinaryOperation.ADD && operation1 != BinaryOperation.SUBTRACT) {
            return null;
        }
        if (operation2 == BinaryOperation.ADD || operation2 == BinaryOperation.SUBTRACT) {
            return new BinaryOperationNode(
                    otherOperand,
                    operation1,
                    new NumberNode(operation1 == operation2
                            ? Math.addExact(number1.getValue(), number2.getValue())
                            : Math.subtractExact(number1.getValue(), number2.getValue())
                    )
            );
        } else if (operation2 == BinaryOperation.EQUALS || operation2 == BinaryOperation.GREATER_THAN || operation2 == BinaryOperation.LESS_THAN) {
            return new BinaryOperationNode(
                    otherOperand,
                    operation2,
                    new NumberNode(operation1 == BinaryOperation.ADD
                            ? Math.subtractExact(number2.getValue(), number1.getValue())
                            : Math.addExact(number2.getValue(), number1.getValue())
                    )
            );
        }
        return null;
    }

    @Override
    protected @Nullable ExpressionNode tryApplyToNumberOp1AnyOp2Number(
            @NotNull NumberNode number1, @NotNull BinaryOperation operation1, @NotNull ExpressionNode otherOperand,
            @NotNull BinaryOperation operation2, @NotNull NumberNode number2
    ) {
        return null;
    }

}
