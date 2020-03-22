package com.github.estekhin.set;

import java.util.List;

import com.github.estekhin.set.parser.BinaryOperation;
import com.github.estekhin.set.parser.BinaryOperationNode;
import com.github.estekhin.set.parser.CallChainNode;
import com.github.estekhin.set.parser.CallNode;
import com.github.estekhin.set.parser.ElementNode;
import com.github.estekhin.set.parser.ExpressionNode;
import com.github.estekhin.set.parser.ExpressionParser;
import com.github.estekhin.set.parser.FilterCallNode;
import com.github.estekhin.set.parser.MapCallNode;
import com.github.estekhin.set.parser.NumberNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StreamExpressionTransformer {

    public @NotNull String transform(@NotNull String expression) {
        return process(new ExpressionParser(expression).parse()).toString();
    }

    private @NotNull CallChainNode process(@NotNull CallChainNode callChainNode) {
        ExpressionNode filterOperand = null;
        ExpressionNode mapOperand = new ElementNode();
        for (CallNode callNode : callChainNode.getCalls()) {
            if (callNode instanceof FilterCallNode) {
                filterOperand = appendFilter(filterOperand, mapOperand, callNode.getOperand());
            } else if (callNode instanceof MapCallNode) {
                mapOperand = appendMap(mapOperand, callNode.getOperand());
            } else {
                throw new IllegalStateException("unexpected CallNode: " + callNode);
            }
        }
        if (filterOperand == null) {
            filterOperand = new BinaryOperationNode(new NumberNode(1), BinaryOperation.EQUALS, new NumberNode(1));
        }
        return new CallChainNode(List.of(
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
