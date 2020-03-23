package com.github.estekhin.set.simplify;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.Nodes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class SameOperandTransformer implements BinaryOperationTransformer {

    @Override
    public @Nullable ExpressionNode tryApply(@NotNull ExpressionNode operand1, @NotNull BinaryOperation operation, @NotNull ExpressionNode operand2) {
        if (!operand1.equals(operand2)) {
            return null;
        }
        switch (operation) {
            case ADD:
                return Nodes.op(operand1, BinaryOperation.MULTIPLY, Nodes.number(2));
            case SUBTRACT:
                return Nodes.number(0);
            case EQUALS:
                return Nodes.bool(true);
            case GREATER_THAN:
            case LESS_THAN:
                return Nodes.bool(false);
            case AND:
            case OR:
                return operand1;
            default:
                return null;
        }
    }

}
