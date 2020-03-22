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
        ExpressionNode transformedOperand = transform(node.getOperand());
        return node.getOperand().equals(transformedOperand)
                ? node
                : new FilterCallNode(transformedOperand);
    }

    @Override
    public @NotNull CallNode visitMapCallNode(@NotNull MapCallNode node) {
        ExpressionNode transformedOperand = transform(node.getOperand());
        return node.getOperand().equals(transformedOperand)
                ? node
                : new MapCallNode(transformedOperand);
    }

    private static @NotNull ExpressionNode transform(@NotNull ExpressionNode node) {
        ExpressionNode previous = node;
        ExpressionNode next = Objects.requireNonNull(previous.visit(new ExpressionVisitor()));
        while (!next.equals(previous)) {
            previous = next;
            next = Objects.requireNonNull(previous.visit(new ExpressionVisitor()));
        }
        return next;
    }

}
