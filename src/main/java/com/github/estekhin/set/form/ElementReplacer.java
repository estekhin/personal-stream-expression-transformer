package com.github.estekhin.set.form;

import java.util.Objects;

import com.github.estekhin.set.ast.BinaryOperationNode;
import com.github.estekhin.set.ast.BooleanNode;
import com.github.estekhin.set.ast.ElementNode;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.NodeVisitor;
import com.github.estekhin.set.ast.NumberNode;
import org.jetbrains.annotations.NotNull;

final class ElementReplacer implements NodeVisitor<ExpressionNode> {

    private final @NotNull ExpressionNode replacement;


    ElementReplacer(@NotNull ExpressionNode replacement) {
        this.replacement = replacement;
    }


    @Override
    public @NotNull ExpressionNode visitNumberNode(@NotNull NumberNode node) {
        return node;
    }

    @Override
    public @NotNull ExpressionNode visitBooleanNode(@NotNull BooleanNode node) {
        return node;
    }

    @Override
    public @NotNull ExpressionNode visitElementNode(@NotNull ElementNode node) {
        return replacement;
    }

    @Override
    public @NotNull ExpressionNode visitBinaryOperationNode(@NotNull BinaryOperationNode node) {
        ExpressionNode transformedOperand1 = Objects.requireNonNull(node.getOperand1().visit(this));
        ExpressionNode transformedOperand2 = Objects.requireNonNull(node.getOperand2().visit(this));
        return node.updateOperands(transformedOperand1, transformedOperand2);
    }

}
