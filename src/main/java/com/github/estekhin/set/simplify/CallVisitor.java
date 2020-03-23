package com.github.estekhin.set.simplify;

import java.util.Objects;

import com.github.estekhin.set.ast.CallNode;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.FilterCallNode;
import com.github.estekhin.set.ast.MapCallNode;
import com.github.estekhin.set.ast.NodeVisitor;
import com.github.estekhin.set.ast.Nodes;
import org.jetbrains.annotations.NotNull;

final class CallVisitor implements NodeVisitor<CallNode> {

    private final @NotNull ExpressionTransformer transformer;


    CallVisitor(@NotNull ExpressionTransformer transformer) {
        this.transformer = transformer;
    }


    @Override
    public @NotNull CallNode visitFilterCallNode(@NotNull FilterCallNode node) {
        ExpressionNode transformedOperand = transform(node.getOperand());
        return node.getOperand().equals(transformedOperand)
                ? node
                : Nodes.filter(transformedOperand);
    }

    @Override
    public @NotNull CallNode visitMapCallNode(@NotNull MapCallNode node) {
        ExpressionNode transformedOperand = transform(node.getOperand());
        return node.getOperand().equals(transformedOperand)
                ? node
                : Nodes.map(transformedOperand);
    }

    private @NotNull ExpressionNode transform(@NotNull ExpressionNode node) {
        ExpressionNode previous = node;
        ExpressionNode next = Objects.requireNonNull(previous.visit(transformer));
        while (!next.equals(previous)) {
            previous = next;
            next = Objects.requireNonNull(previous.visit(transformer));
        }
        return next;
    }

}
