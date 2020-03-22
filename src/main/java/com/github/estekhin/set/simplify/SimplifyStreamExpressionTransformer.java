package com.github.estekhin.set.simplify;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.github.estekhin.set.ast.CallNode;
import com.github.estekhin.set.ast.NodeVisitor;
import com.github.estekhin.set.ast.StreamExpressionNode;
import org.jetbrains.annotations.NotNull;

public final class SimplifyStreamExpressionTransformer implements NodeVisitor<StreamExpressionNode> {

    @Override
    public @NotNull StreamExpressionNode visitStreamExpressionNode(@NotNull StreamExpressionNode node) {
        List<CallNode> transformedCalls = node.getCalls().stream()
                .map(it -> Objects.requireNonNull(it.visit(new CallVisitor())))
                .collect(Collectors.toUnmodifiableList());
        return new StreamExpressionNode(transformedCalls);
    }

}
