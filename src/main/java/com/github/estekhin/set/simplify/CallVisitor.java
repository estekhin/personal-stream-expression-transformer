package com.github.estekhin.set.simplify;

import java.util.Objects;

import com.github.estekhin.set.ast.CallNode;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.FilterCallNode;
import com.github.estekhin.set.ast.MapCallNode;
import com.github.estekhin.set.ast.NodeVisitor;
import org.jetbrains.annotations.NotNull;

final class CallVisitor implements NodeVisitor<CallNode> {

    @Override
    public @NotNull CallNode visitFilterCallNode(@NotNull FilterCallNode node) {
        ExpressionNode transformedOperand = Objects.requireNonNull(node.getOperand().visit(new ExpressionVisitor()));
        if (transformedOperand == node.getOperand()) {
            return node;
        }
        return new FilterCallNode(transformedOperand);
    }

    @Override
    public @NotNull CallNode visitMapCallNode(@NotNull MapCallNode node) {
        ExpressionNode transformedOperand = Objects.requireNonNull(node.getOperand().visit(new ExpressionVisitor()));
        if (transformedOperand == node.getOperand()) {
            return node;
        }
        return new MapCallNode(transformedOperand);
    }

}
