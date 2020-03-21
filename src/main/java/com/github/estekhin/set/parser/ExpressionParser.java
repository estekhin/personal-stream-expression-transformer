package com.github.estekhin.set.parser;

import java.util.ArrayList;
import java.util.List;

import com.github.estekhin.set.ExpressionSyntaxException;
import org.jetbrains.annotations.NotNull;

public final class ExpressionParser {

    private final @NotNull String expression;
    private int index;


    public ExpressionParser(@NotNull String expression) {
        this.expression = expression;
    }


    public @NotNull CallChainNode parse() {
        List<CallNode> calls = new ArrayList<>();
        calls.add(parseCall());
        while (tryConsume(Tokens.CALL_CHAIN_SEPARATOR)) {
            calls.add(parseCall());
        }
        if (index != expression.length()) {
            throw expectedTokens(expression, index, Tokens.CALL_CHAIN_SEPARATOR);
        }
        return new CallChainNode(calls);
    }

    private @NotNull CallNode parseCall() {
        if (tryConsume(Tokens.MAP_START)) {
            ExpressionNode operand = parseExpression();
            consume(Tokens.MAP_END);
            return new MapCallNode(operand);
        } else if (tryConsume(Tokens.FILTER_START)) {
            ExpressionNode operand = parseExpression();
            consume(Tokens.FILTER_END);
            return new FilterCallNode(operand);
        } else {
            throw expectedTokens(expression, index, Tokens.MAP_START, Tokens.FILTER_START);
        }
    }

    private @NotNull ExpressionNode parseExpression() {
        if (tryConsume(Tokens.ELEMENT)) {
            return new ElementNode();
        } else if (tryConsume(Tokens.BINARY_EXPRESSION_START)) {
            ExpressionNode operand1 = parseExpression();
            BinaryOperation operation = parseOperation();
            ExpressionNode operand2 = parseExpression();
            consume(Tokens.BINARY_EXPRESSION_END);
            return new BinaryOperationNode(operand1, operation, operand2);
        } else if (tryConsume(Tokens.UNARY_MINUS)) {
            return new NumberNode(Math.subtractExact(0L, parseNumber()));
        } else if (peekDigit()) {
            return new NumberNode(parseNumber());
        } else {
            throw expectedTokens(expression, index, Tokens.ELEMENT, Tokens.BINARY_EXPRESSION_START, Tokens.UNARY_MINUS, "<digit>");
        }
    }

    private @NotNull BinaryOperation parseOperation() {
        for (BinaryOperation operation : BinaryOperation.values()) {
            if (tryConsume(operation.getToken())) {
                return operation;
            }
        }
        throw expectedTokens(expression, index, (Object[]) BinaryOperation.values());
    }


    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean peekDigit() {
        return index < expression.length() && isDigit(expression.charAt(index));
    }

    private long parseNumber() {
        int beginIndex = index;
        while (peekDigit()) {
            index++;
        }
        String value = expression.substring(beginIndex, index);
        if (value.isEmpty()) {
            throw expectedTokens(expression, beginIndex, "<number>");
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ignored) {
            throw expectedTokens(expression, beginIndex, "<number>");
        }
    }


    private boolean tryConsume(@NotNull String token) {
        if (expression.startsWith(token, index)) {
            index += token.length();
            return true;
        } else {
            return false;
        }
    }

    private void consume(@NotNull String token) {
        if (expression.startsWith(token, index)) {
            index += token.length();
        } else {
            throw expectedTokens(expression, index, token);
        }
    }


    private static @NotNull ExpressionSyntaxException expectedTokens(@NotNull String expression, int index, @NotNull Object... expectedTokens) {
        if (expectedTokens.length == 0) {
            throw new IllegalArgumentException("expectedTokens is empty");
        } else if (expectedTokens.length == 1) {
            return new ExpressionSyntaxException(String.format(
                    "expected '%s' after '%s', got '%s'",
                    expectedTokens[0],
                    expression.substring(0, index), expression.substring(index)
            ));
        } else {
            StringBuilder expectedToken = new StringBuilder();
            expectedToken.append('\'').append(expectedTokens[0]).append('\'');
            for (int i = 1; i < expectedTokens.length - 1; i++) {
                expectedToken
                        .append(", ")
                        .append('\'').append(expectedTokens[i]).append('\'');
            }
            expectedToken
                    .append(" or ")
                    .append('\'').append(expectedTokens[expectedTokens.length - 1]).append('\'');
            return new ExpressionSyntaxException(String.format(
                    "expected %s after '%s', got '%s'",
                    expectedToken,
                    expression.substring(0, index), expression.substring(index)
            ));
        }
    }

}
