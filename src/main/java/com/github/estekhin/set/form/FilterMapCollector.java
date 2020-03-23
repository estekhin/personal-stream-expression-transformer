package com.github.estekhin.set.form;

import java.util.Objects;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.FilterCallNode;
import com.github.estekhin.set.ast.MapCallNode;
import com.github.estekhin.set.ast.NodeVisitor;
import com.github.estekhin.set.ast.Nodes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class FilterMapCollector implements NodeVisitor<Void> {

    private @Nullable ExpressionNode filterOperandCollector;
    private @Nullable ExpressionNode mapOperandCollector;


    @Override
    public @Nullable Void visitFilterCallNode(@NotNull FilterCallNode node) {
        filterOperandCollector = applyCurrentFilter(applyCurrentMap(node.getOperand()));
        return null;
    }

    @Override
    public @Nullable Void visitMapCallNode(@NotNull MapCallNode node) {
        mapOperandCollector = applyCurrentMap(node.getOperand());
        return null;
    }

    private @NotNull ExpressionNode applyCurrentFilter(@NotNull ExpressionNode node) {
        return filterOperandCollector != null
                ? Nodes.op(filterOperandCollector, BinaryOperation.AND, node)
                : node;
    }

    private @NotNull ExpressionNode applyCurrentMap(@NotNull ExpressionNode node) {
        return mapOperandCollector != null
                ? Objects.requireNonNull(node.visit(new ElementReplacer(mapOperandCollector)))
                : node;
    }

    @NotNull ExpressionNode getFilterOperand() {
        return filterOperandCollector != null
                ? filterOperandCollector
                : Nodes.op(Nodes.number(1), BinaryOperation.EQUALS, Nodes.number(1));
    }

    @NotNull ExpressionNode getMapOperand() {
        return mapOperandCollector != null
                ? mapOperandCollector
                : Nodes.element();
    }

}
