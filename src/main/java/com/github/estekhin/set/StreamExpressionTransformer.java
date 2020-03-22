package com.github.estekhin.set;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.BinaryOperationNode;
import com.github.estekhin.set.ast.CallNode;
import com.github.estekhin.set.ast.CallNodeVisitor;
import com.github.estekhin.set.ast.ElementNode;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.ExpressionNodeTransformer;
import com.github.estekhin.set.ast.FilterCallNode;
import com.github.estekhin.set.ast.MapCallNode;
import com.github.estekhin.set.ast.NumberNode;
import com.github.estekhin.set.ast.StreamExpressionNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StreamExpressionTransformer {

    public @NotNull String transform(@NotNull String expression) {
        return process(new StreamExpressionParser(expression).parse()).toString();
    }

    private @NotNull StreamExpressionNode process(@NotNull StreamExpressionNode streamExpression) {
        FilterMapTransformer filterMapTransformer = new FilterMapTransformer();
        for (CallNode call : streamExpression.getCalls()) {
            call.visit(filterMapTransformer);
        }
        return filterMapTransformer.create();
    }

    private static final class FilterMapTransformer implements CallNodeVisitor {

        private @Nullable ExpressionNode filterOperandCollector;
        private @Nullable ExpressionNode mapOperandCollector;

        @Override
        public void visitFilterCallNode(@NotNull FilterCallNode node) {
            filterOperandCollector = applyCurrentFilter(applyCurrentMap(node.getOperand()));
        }

        @Override
        public void visitMapCallNode(@NotNull MapCallNode node) {
            mapOperandCollector = applyCurrentMap(node.getOperand());
        }

        private @NotNull ExpressionNode applyCurrentFilter(@NotNull ExpressionNode node) {
            return filterOperandCollector != null
                    ? new BinaryOperationNode(filterOperandCollector, BinaryOperation.AND, node)
                    : node;
        }

        private @NotNull ExpressionNode applyCurrentMap(@NotNull ExpressionNode node) {
            return mapOperandCollector != null
                    ? node.transform(new ElementReplacer(mapOperandCollector))
                    : node;
        }

        public @NotNull StreamExpressionNode create() {
            return new StreamExpressionNode(
                    new FilterCallNode(filterOperandCollector != null
                            ? filterOperandCollector
                            : new BinaryOperationNode(new NumberNode(1), BinaryOperation.EQUALS, new NumberNode(1))
                    ),
                    new MapCallNode(mapOperandCollector != null
                            ? mapOperandCollector
                            : new ElementNode()
                    )
            );
        }

    }

    private static final class ElementReplacer implements ExpressionNodeTransformer {

        private final @NotNull ExpressionNode replacement;


        ElementReplacer(@NotNull ExpressionNode replacement) {
            this.replacement = replacement;
        }

        @Override
        public @NotNull ExpressionNode transformElementNode(@NotNull ElementNode node) {
            return replacement;
        }

    }

}
