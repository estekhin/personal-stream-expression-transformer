package com.github.estekhin.set;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.BinaryOperationNode;
import com.github.estekhin.set.ast.CallNode;
import com.github.estekhin.set.ast.ElementNode;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.ExpressionType;
import com.github.estekhin.set.ast.FilterCallNode;
import com.github.estekhin.set.ast.MapCallNode;
import com.github.estekhin.set.ast.NodeVisitor;
import com.github.estekhin.set.ast.NumberNode;
import com.github.estekhin.set.ast.StreamExpressionNode;
import org.jetbrains.annotations.NotNull;

final class SimplifyTransformer implements NodeVisitor<StreamExpressionNode> {

    @Override
    public @NotNull StreamExpressionNode visitStreamExpressionNode(@NotNull StreamExpressionNode node) {
        CallVisitor callVisitor = new CallVisitor();
        List<CallNode> transformedCalls = node.getCalls().stream()
                .map(it -> Objects.requireNonNull(it.visit(callVisitor)))
                .collect(Collectors.toUnmodifiableList());
        return new StreamExpressionNode(transformedCalls);
    }


    private static final class CallVisitor implements NodeVisitor<CallNode> {

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

    static final class ExpressionVisitor implements NodeVisitor<ExpressionNode> {

        @Override
        public @NotNull ExpressionNode visitNumberNode(@NotNull NumberNode node) {
            return node;
        }

        @Override
        public @NotNull ExpressionNode visitElementNode(@NotNull ElementNode node) {
            return node;
        }

        @Override
        public @NotNull ExpressionNode visitBinaryOperationNode(@NotNull BinaryOperationNode node) {
            ExpressionNode transformedOperand1 = Objects.requireNonNull(node.getOperand1().visit(this));
            ExpressionNode transformedOperand2 = Objects.requireNonNull(node.getOperand2().visit(this));
            if (node.getOperation().getResultType() == ExpressionType.INTEGER) {
                if (transformedOperand1 instanceof NumberNode && transformedOperand2 instanceof NumberNode) {
                    return foldConstant((NumberNode) transformedOperand1, node.getOperation(), (NumberNode) transformedOperand2);
                }
            }
            if (transformedOperand1 == node.getOperand1() && transformedOperand2 == node.getOperand2()) {
                return node;
            }
            return new BinaryOperationNode(
                    transformedOperand1,
                    node.getOperation(),
                    transformedOperand2
            );
        }

        private @NotNull ExpressionNode foldConstant(@NotNull NumberNode operand1, @NotNull BinaryOperation operation, @NotNull NumberNode operand2) {
            switch (operation) {
                case ADD:
                    return new NumberNode(Math.addExact(operand1.getValue(), operand2.getValue()));
                case SUBTRACT:
                    return new NumberNode(Math.subtractExact(operand1.getValue(), operand2.getValue()));
                case MULTIPLY:
                    return new NumberNode(Math.multiplyExact(operand1.getValue(), operand2.getValue()));
                default:
                    throw new IllegalArgumentException("unexpected BinaryOperation: " + operation);
            }
        }

    }

}
