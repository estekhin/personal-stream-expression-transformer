package com.github.estekhin.set.parser;

import org.jetbrains.annotations.NotNull;

final class Tokens {

    static final @NotNull String CALL_CHAIN_SEPARATOR = "%>%";
    static final @NotNull String MAP_START = "map{";
    static final @NotNull String MAP_END = "}";
    static final @NotNull String FILTER_START = "filter{";
    static final @NotNull String FILTER_END = "}";
    static final @NotNull String ELEMENT = "element";
    static final @NotNull String UNARY_MINUS = "-";
    static final @NotNull String BINARY_EXPRESSION_START = "(";
    static final @NotNull String BINARY_EXPRESSION_END = ")";


    private Tokens() {
    }

}
