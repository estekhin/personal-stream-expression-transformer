package com.github.estekhin.set.simplify;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.Nodes;
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
                return Nodes.number(Math.addExact(operand1.getValue(), operand2.getValue()));
            case SUBTRACT:
                return Nodes.number(Math.subtractExact(operand1.getValue(), operand2.getValue()));
            case MULTIPLY:
                return Nodes.number(Math.multiplyExact(operand1.getValue(), operand2.getValue()));
            case EQUALS:
                return Nodes.bool(operand1.getValue() == operand2.getValue());
            case GREATER_THAN:
                return Nodes.bool(operand1.getValue() > operand2.getValue());
            case LESS_THAN:
                return Nodes.bool(operand1.getValue() < operand2.getValue());
            default:
                return null;
        }
    }

}
