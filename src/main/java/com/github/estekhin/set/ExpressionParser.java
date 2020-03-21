package com.github.estekhin.set;

import org.jetbrains.annotations.NotNull;

final class ExpressionParser {

    private final @NotNull String expression;
    private int index;


    ExpressionParser(@NotNull String expression) {
        this.expression = expression;
    }


    void parseCallChain() {
        /*
        <call-chain> ::= <call> | <call> "%>%" <call-chain>
         */
        parseCall();
        while (expression.startsWith("%>%", index)) {
            index += 3; // "%>%".length()
            parseCall();
        }
        if (index != expression.length()) {
            throw new ExpressionSyntaxException(String.format(
                    "[%d] expected call chain separator, got %s",
                    index,
                    expression.substring(index)
            ));
        }
    }

    private void parseCall() {
        /*
        <call> ::= <map-call> | <filter-call>
            <map-call> ::= "map{" <expression> "}"
            <filter-call> ::= "filter{" <expression> "}"
         */
        if (expression.startsWith("map{", index)) {
            index += 4; // "map{".length();
        } else if (expression.startsWith("filter{", index)) {
            index += 7; // "filter{".length();
        } else {
            throw new ExpressionSyntaxException(String.format(
                    "[%d] expected 'map{' or 'filter{', got %s",
                    index,
                    expression.substring(index)
            ));
        }
        parseExpression();
        if (index == expression.length() || expression.charAt(index) != '}') {
            throw new ExpressionSyntaxException(String.format(
                    "[%d] expected call end, got %s",
                    index,
                    expression.substring(index)
            ));
        }
        index += 1; // "}".length()
    }

    private void parseExpression() {
        /*
        <expression> ::= "element" | <constant-expression> | <binary-expression>
            <constant-expression> ::= "-" <number> | <number>
            <binary-expression> ::= "(" <expression> <operation> <expression> ")"
         */
        if (expression.startsWith("element", index)) {
            index += 7; // "element".length();
        } else if (expression.charAt(index) == '-') {
            index += 1; // "-".length()
            parseNumber();
        } else if (expression.charAt(index) >= '0' && expression.charAt(index) <= '9') {
            parseNumber();
        } else if (expression.charAt(index) == '(') {
            index += 1; // "(".length()
            parseExpression();
            parseOperation();
            parseExpression();
            if (index == expression.length() || expression.charAt(index) != ')') {
                throw new ExpressionSyntaxException(String.format(
                        "[%d] expected binary expression end, got %s",
                        index,
                        expression.substring(index)
                ));
            }
            index += 1; // ")".length()
        } else {
            throw new ExpressionSyntaxException(String.format(
                    "[%d] expected expression, got %s",
                    index,
                    expression.substring(index)
            ));
        }
    }

    private void parseNumber() {
        /*
        <number> ::= <digit> | <digit> <number>
            <digit>   ::= "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
         */
        while (expression.charAt(index) >= '0' && expression.charAt(index) <= '9') {
            index++;
        }
    }

    private void parseOperation() {
        /*
        <operation> ::= "+" | "-" | "*" | ">" | "<" | "=" | "&" | "|"
         */
        char c = expression.charAt(index);
        switch (c) {
            case '+':
            case '-':
            case '*':
            case '<':
            case '>':
            case '=':
            case '&':
            case '|':
                index += 1;
                break;
            default:
                throw new ExpressionSyntaxException(String.format(
                        "[%d] expected operation, got %s",
                        index,
                        expression.substring(index)
                ));
        }
    }

}
