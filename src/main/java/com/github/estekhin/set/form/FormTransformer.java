package com.github.estekhin.set.form;

import com.github.estekhin.set.ast.FilterCallNode;
import com.github.estekhin.set.ast.MapCallNode;
import com.github.estekhin.set.ast.NodeVisitor;
import com.github.estekhin.set.ast.StreamExpressionNode;
import org.jetbrains.annotations.NotNull;

public final class FormTransformer implements NodeVisitor<StreamExpressionNode> {

    @Override
    public @NotNull StreamExpressionNode visitStreamExpressionNode(@NotNull StreamExpressionNode node) {
        FilterMapCollector filterMapCollector = new FilterMapCollector();
        node.getCalls().forEach(it -> it.visit(filterMapCollector));
        return new StreamExpressionNode(
                new FilterCallNode(filterMapCollector.getFilterOperand()),
                new MapCallNode(filterMapCollector.getMapOperand())
        );
    }

}
