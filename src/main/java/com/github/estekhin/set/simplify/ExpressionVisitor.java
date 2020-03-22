package com.github.estekhin.set.simplify;

import java.util.Objects;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.BinaryOperationNode;
import com.github.estekhin.set.ast.ElementNode;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.ExpressionType;
import com.github.estekhin.set.ast.NodeVisitor;
import com.github.estekhin.set.ast.NumberNode;
import org.jetbrains.annotations.NotNull;

final class ExpressionVisitor implements NodeVisitor<ExpressionNode> {

    @Override
    public @NotNull ExpressionNode visitNumberNode(@NotNull NumberNode node) {
        return node;
    }

    @Override
    public @NotNull ExpressionNode visitElementNode(@NotNull ElementNode node) {
        return node;
    }

    @Override
    public @NotNull ExpressionNode visitBinaryOperationNode(@NotNull BinaryOperationNode node) {
        ExpressionNode transformedOperand1 = Objects.requireNonNull(node.getOperand1().visit(this));
        ExpressionNode transformedOperand2 = Objects.requireNonNull(node.getOperand2().visit(this));
        if (node.getOperation().getResultType() == ExpressionType.INTEGER) {
            if (transformedOperand1 instanceof NumberNode && transformedOperand2 instanceof NumberNode) {
                return foldConstant((NumberNode) transformedOperand1, node.getOperation(), (NumberNode) transformedOperand2);
            }
        }
        if (transformedOperand1 == node.getOperand1() && transformedOperand2 == node.getOperand2()) {
            return node;
        }
        return new BinaryOperationNode(
                transformedOperand1,
                node.getOperation(),
                transformedOperand2
        );
    }

    private @NotNull ExpressionNode foldConstant(@NotNull NumberNode operand1, @NotNull BinaryOperation operation, @NotNull NumberNode operand2) {
        switch (operation) {
            case ADD:
                return new NumberNode(Math.addExact(operand1.getValue(), operand2.getValue()));
            case SUBTRACT:
                return new NumberNode(Math.subtractExact(operand1.getValue(), operand2.getValue()));
            case MULTIPLY:
                return new NumberNode(Math.multiplyExact(operand1.getValue(), operand2.getValue()));
            default:
                throw new IllegalArgumentException("unexpected BinaryOperation: " + operation);
        }
    }

}
