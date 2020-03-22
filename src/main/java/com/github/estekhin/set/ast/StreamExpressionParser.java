package com.github.estekhin.set.ast;

import java.util.ArrayList;
import java.util.List;

import com.github.estekhin.set.ExpressionSyntaxException;
import org.jetbrains.annotations.NotNull;

public final class StreamExpressionParser {

    private final @NotNull String source;
    private int index;


    public StreamExpressionParser(@NotNull String source) {
        this.source = source;
    }


    public @NotNull StreamExpressionNode parse() {
        List<CallNode> calls = new ArrayList<>();
        calls.add(parseCall());
        while (tryConsume(StreamExpressionNode.CALL_CHAIN_SEPARATOR)) {
            calls.add(parseCall());
        }
        if (index != source.length()) {
            throw newExpectedTokensException(source, index, StreamExpressionNode.CALL_CHAIN_SEPARATOR);
        }
        return new StreamExpressionNode(calls);
    }

    private @NotNull CallNode parseCall() {
        if (tryConsume(FilterCallNode.FILTER_START)) {
            ExpressionNode operand = parseExpression();
            consume(FilterCallNode.FILTER_END);
            return new FilterCallNode(operand);
        } else if (tryConsume(MapCallNode.MAP_START)) {
            ExpressionNode operand = parseExpression();
            consume(MapCallNode.MAP_END);
            return new MapCallNode(operand);
        } else {
            throw newExpectedTokensException(source, index, MapCallNode.MAP_START, FilterCallNode.FILTER_START);
        }
    }

    private @NotNull ExpressionNode parseExpression() {
        if (tryConsume(ElementNode.ELEMENT)) {
            return new ElementNode();
        } else if (tryConsume(BinaryOperationNode.BINARY_EXPRESSION_START)) {
            ExpressionNode operand1 = parseExpression();
            BinaryOperation operation = parseOperation();
            ExpressionNode operand2 = parseExpression();
            consume(BinaryOperationNode.BINARY_EXPRESSION_END);
            return new BinaryOperationNode(operand1, operation, operand2);
        } else if (tryConsume(NumberNode.UNARY_MINUS)) {
            return new NumberNode(Math.subtractExact(0L, parseNumber()));
        } else if (peekDigit()) {
            return new NumberNode(parseNumber());
        } else {
            throw newExpectedTokensException(source, index, ElementNode.ELEMENT, BinaryOperationNode.BINARY_EXPRESSION_START, NumberNode.UNARY_MINUS, "<digit>");
        }
    }

    private @NotNull BinaryOperation parseOperation() {
        for (BinaryOperation operation : BinaryOperation.values()) {
            if (tryConsume(operation.getToken())) {
                return operation;
            }
        }
        throw newExpectedTokensException(source, index, (Object[]) BinaryOperation.values());
    }


    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean peekDigit() {
        return index < source.length() && isDigit(source.charAt(index));
    }

    private long parseNumber() {
        int beginIndex = index;
        while (peekDigit()) {
            index++;
        }
        String value = source.substring(beginIndex, index);
        if (value.isEmpty()) {
            throw newExpectedTokensException(source, beginIndex, "<number>");
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ignored) {
            throw newExpectedTokensException(source, beginIndex, "<number>");
        }
    }


    private boolean tryConsume(@NotNull String token) {
        if (source.startsWith(token, index)) {
            index += token.length();
            return true;
        } else {
            return false;
        }
    }

    private void consume(@NotNull String token) {
        if (source.startsWith(token, index)) {
            index += token.length();
        } else {
            throw newExpectedTokensException(source, index, token);
        }
    }


    private static @NotNull ExpressionSyntaxException newExpectedTokensException(@NotNull String source, int index, @NotNull Object... expectedTokens) {
        return new ExpressionSyntaxException(String.format(
                "expected %s after '%s', got '%s'",
                expectedTokensAsString(expectedTokens),
                source.substring(0, index), source.substring(index)
        ));
    }

    private static @NotNull String expectedTokensAsString(@NotNull Object... expectedTokens) {
        if (expectedTokens.length == 0) {
            throw new IllegalArgumentException("expectedTokens is empty");
        }
        StringBuilder expectedToken = new StringBuilder();
        expectedToken.append('\'').append(expectedTokens[0]).append('\'');
        for (int i = 1; i < expectedTokens.length - 1; i++) {
            expectedToken.append(", ")
                    .append('\'').append(expectedTokens[i]).append('\'');
        }
        if (expectedTokens.length > 1) {
            expectedToken.append(" or ")
                    .append('\'').append(expectedTokens[expectedTokens.length - 1]).append('\'');
        }
        return expectedToken.toString();
    }

}
