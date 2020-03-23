package com.github.estekhin.set.simplify;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.ElementNode;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.Nodes;
import com.github.estekhin.set.ast.NumberNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ElementFirstTransformer implements BinaryOperationTransformer {

    @Override
    public @Nullable ExpressionNode tryApply(@NotNull ExpressionNode operand1, @NotNull BinaryOperation operation, @NotNull ExpressionNode operand2) {
        if (operand1 instanceof NumberNode && operand2 instanceof ElementNode) {
            return tryApplyToNumberOpElement((NumberNode) operand1, operation, (ElementNode) operand2);
        }
        return null;
    }

    private @Nullable ExpressionNode tryApplyToNumberOpElement(@NotNull NumberNode operand1, @NotNull BinaryOperation operation, @NotNull ElementNode operand2) {
        switch (operation) {
            case ADD:
                return Nodes.op(operand2, BinaryOperation.ADD, operand1);
            case MULTIPLY:
                return Nodes.op(operand2, BinaryOperation.MULTIPLY, operand1);
            case EQUALS:
                return Nodes.op(operand2, BinaryOperation.EQUALS, operand1);
            case GREATER_THAN:
                return Nodes.op(operand2, BinaryOperation.LESS_THAN, operand1);
            case LESS_THAN:
                return Nodes.op(operand2, BinaryOperation.GREATER_THAN, operand1);
            default:
                return null;
        }
    }

}
