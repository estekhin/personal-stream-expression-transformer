package com.github.estekhin.set;

import java.util.Objects;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.BinaryOperationNode;
import com.github.estekhin.set.ast.ElementNode;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.FilterCallNode;
import com.github.estekhin.set.ast.MapCallNode;
import com.github.estekhin.set.ast.NodeVisitor;
import com.github.estekhin.set.ast.NumberNode;
import com.github.estekhin.set.ast.StreamExpressionNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class FilterMapTransformer implements NodeVisitor<StreamExpressionNode> {

    @Override
    public @NotNull StreamExpressionNode visitStreamExpressionNode(@NotNull StreamExpressionNode node) {
        FilterMapCollector filterMapCollector = new FilterMapCollector();
        node.getCalls().forEach(it -> it.visit(filterMapCollector));
        return new StreamExpressionNode(
                new FilterCallNode(filterMapCollector.getFilterOperand()),
                new MapCallNode(filterMapCollector.getMapOperand())
        );
    }


    private static final class FilterMapCollector implements NodeVisitor<Void> {

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
                    ? new BinaryOperationNode(filterOperandCollector, BinaryOperation.AND, node)
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
                    : new BinaryOperationNode(new NumberNode(1), BinaryOperation.EQUALS, new NumberNode(1));
        }

        @NotNull ExpressionNode getMapOperand() {
            return mapOperandCollector != null
                    ? mapOperandCollector
                    : new ElementNode();
        }

    }

    private static final class ElementReplacer implements NodeVisitor<ExpressionNode> {

        private final @NotNull ExpressionNode replacement;

        ElementReplacer(@NotNull ExpressionNode replacement) {
            this.replacement = replacement;
        }

        @Override
        public @NotNull ExpressionNode visitNumberNode(@NotNull NumberNode node) {
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

}
