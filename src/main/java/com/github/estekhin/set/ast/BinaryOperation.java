package com.github.estekhin.set.ast;

import org.jetbrains.annotations.NotNull;

public enum BinaryOperation {

    ADD("+", ExpressionType.INTEGER, ExpressionType.INTEGER),
    SUBTRACT("-", ExpressionType.INTEGER, ExpressionType.INTEGER),
    MULTIPLY("*", ExpressionType.INTEGER, ExpressionType.INTEGER),
    GREATER_THAN(">", ExpressionType.INTEGER, ExpressionType.BOOLEAN),
    LESS_THAN("<", ExpressionType.INTEGER, ExpressionType.BOOLEAN),
    EQUALS("=", ExpressionType.INTEGER, ExpressionType.BOOLEAN),
    AND("&", ExpressionType.BOOLEAN, ExpressionType.BOOLEAN),
    OR("|", ExpressionType.BOOLEAN, ExpressionType.BOOLEAN);


    private final @NotNull String token;
    private final @NotNull ExpressionType operandsType;
    private final @NotNull ExpressionType resultType;


    BinaryOperation(@NotNull String token, @NotNull ExpressionType operandsType, @NotNull ExpressionType resultType) {
        this.token = token;
        this.operandsType = operandsType;
        this.resultType = resultType;
    }


    public @NotNull String getToken() {
        return token;
    }

    public @NotNull ExpressionType getOperandsType() {
        return operandsType;
    }

    public @NotNull ExpressionType getResultType() {
        return resultType;
    }


    @Override
    public String toString() {
        return token;
    }

}
