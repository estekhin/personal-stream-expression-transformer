package com.github.estekhin.set.simplify;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.BinaryOperationNode;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.NumberNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class NegativeConstantBinaryOperationTransformer implements BinaryOperationTransformer {

    @Override
    public @Nullable ExpressionNode tryApply(@NotNull ExpressionNode operand1, @NotNull BinaryOperation operation, @NotNull ExpressionNode operand2) {
        if (operation == BinaryOperation.ADD
                && operand2 instanceof NumberNode
        ) {
            long value = ((NumberNode) operand2).getValue();
            if (value < 0) {
                // any + <-number> => any - <number>
                return new BinaryOperationNode(
                        operand1,
                        BinaryOperation.SUBTRACT,
                        new NumberNode(Math.subtractExact(0L, value))
                );
            }
        } else if (operation == BinaryOperation.SUBTRACT
                && operand2 instanceof NumberNode
        ) {
            long value = ((NumberNode) operand2).getValue();
            if (value < 0) {
                // any - <-number> => any + <number>
                return new BinaryOperationNode(
                        operand1,
                        BinaryOperation.ADD,
                        new NumberNode(Math.subtractExact(0L, value))
                );
            }
        } else if (operation == BinaryOperation.ADD
                && operand1 instanceof NumberNode
        ) {
            long value = ((NumberNode) operand1).getValue();
            if (value < 0) {
                // <-number> + any => any - <number>
                return new BinaryOperationNode(
                        operand2,
                        BinaryOperation.SUBTRACT,
                        new NumberNode(Math.subtractExact(0L, value))
                );
            }
        }
        return null;
    }

}
