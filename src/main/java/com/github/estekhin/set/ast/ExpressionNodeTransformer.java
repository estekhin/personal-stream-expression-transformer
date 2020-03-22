package com.github.estekhin.set.ast;

import org.jetbrains.annotations.NotNull;

public interface ExpressionNodeTransformer {

    default @NotNull ExpressionNode transformNumberNode(@NotNull NumberNode node) {
        return node;
    }

    default @NotNull ExpressionNode transformElementNode(@NotNull ElementNode node) {
        return node;
    }

    default @NotNull ExpressionNode transformBinaryOperationNode(@NotNull BinaryOperationNode node) {
        ExpressionNode transformedOperand1 = node.getOperand1().transform(this);
        ExpressionNode transformedOperand2 = node.getOperand2().transform(this);
        if (transformedOperand1 == node.getOperand1() && transformedOperand2 == node.getOperand2()) {
            return node;
        }
        return new BinaryOperationNode(
                transformedOperand1,
                node.getOperation(),
                transformedOperand2
        );
    }

}
