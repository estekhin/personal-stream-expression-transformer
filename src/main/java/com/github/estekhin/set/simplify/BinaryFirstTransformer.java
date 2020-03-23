package com.github.estekhin.set.simplify;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.BinaryOperationNode;
import com.github.estekhin.set.ast.ElementNode;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.Nodes;
import com.github.estekhin.set.ast.NumberNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class BinaryFirstTransformer implements BinaryOperationTransformer {

    @Override
    public @Nullable ExpressionNode tryApply(@NotNull ExpressionNode operand1, @NotNull BinaryOperation operation, @NotNull ExpressionNode operand2) {
        if ((operand1 instanceof NumberNode || operand1 instanceof ElementNode) && operand2 instanceof BinaryOperationNode) {
            return tryApplyToNumberOpElement(operand1, operation, (BinaryOperationNode) operand2);
        }
        return null;
    }

    private @Nullable ExpressionNode tryApplyToNumberOpElement(@NotNull ExpressionNode numberOrElement, @NotNull BinaryOperation operation, @NotNull BinaryOperationNode op) {
        switch (operation) {
            case ADD:
                return Nodes.op(op, BinaryOperation.ADD, numberOrElement);
            case MULTIPLY:
                return Nodes.op(op, BinaryOperation.MULTIPLY, numberOrElement);
            case EQUALS:
                return Nodes.op(op, BinaryOperation.EQUALS, numberOrElement);
            case GREATER_THAN:
                return Nodes.op(op, BinaryOperation.LESS_THAN, numberOrElement);
            case LESS_THAN:
                return Nodes.op(op, BinaryOperation.GREATER_THAN, numberOrElement);
            default:
                return null;
        }
    }

}
