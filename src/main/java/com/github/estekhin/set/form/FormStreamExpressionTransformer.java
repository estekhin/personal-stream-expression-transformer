package com.github.estekhin.set.form;

import com.github.estekhin.set.ast.NodeVisitor;
import com.github.estekhin.set.ast.Nodes;
import com.github.estekhin.set.ast.StreamExpressionNode;
import org.jetbrains.annotations.NotNull;

public final class FormStreamExpressionTransformer implements NodeVisitor<StreamExpressionNode> {

    @Override
    public @NotNull StreamExpressionNode visitStreamExpressionNode(@NotNull StreamExpressionNode node) {
        FilterMapCollector filterMapCollector = new FilterMapCollector();
        node.getCalls().forEach(it -> it.visit(filterMapCollector));
        return Nodes.expression(
                Nodes.filter(filterMapCollector.getFilterOperand()),
                Nodes.map(filterMapCollector.getMapOperand())
        );
    }

}
