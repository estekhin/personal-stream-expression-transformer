package com.github.estekhin.set;

import java.util.List;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.BinaryOperationNode;
import com.github.estekhin.set.ast.CallNode;
import com.github.estekhin.set.ast.ElementNode;
import com.github.estekhin.set.ast.ExpressionNode;
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
        ExpressionNode filterOperand = null;
        ExpressionNode mapOperand = new ElementNode();
        for (CallNode call : streamExpression.getCalls()) {
            if (call instanceof FilterCallNode) {
                filterOperand = appendFilter(filterOperand, mapOperand, call.getOperand());
            } else if (call instanceof MapCallNode) {
                mapOperand = appendMap(mapOperand, call.getOperand());
            } else {
                throw new IllegalStateException("unexpected CallNode: " + call);
            }
        }
        if (filterOperand == null) {
            filterOperand = new BinaryOperationNode(new NumberNode(1), BinaryOperation.EQUALS, new NumberNode(1));
        }
        return new StreamExpressionNode(List.of(
                new FilterCallNode(filterOperand),
                new MapCallNode(mapOperand)
        ));
    }

    private @NotNull ExpressionNode appendFilter(
            @Nullable ExpressionNode currentFilterOperand, @NotNull ExpressionNode currentMapOperand,
            @NotNull ExpressionNode newFilterOperand
    ) {
        newFilterOperand = newFilterOperand.replaceElement(currentMapOperand);
        if (currentFilterOperand == null) {
            return newFilterOperand;
        } else {
            return new BinaryOperationNode(currentFilterOperand, BinaryOperation.AND, newFilterOperand);
        }
    }

    private @NotNull ExpressionNode appendMap(
            @NotNull ExpressionNode currentMapOperand,
            @NotNull ExpressionNode newMapOperand
    ) {
        return newMapOperand.replaceElement(currentMapOperand);
    }

}
