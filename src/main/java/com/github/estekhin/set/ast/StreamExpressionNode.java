package com.github.estekhin.set.ast;

import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

public final class StreamExpressionNode extends Node {

    public static final @NotNull String CALL_CHAIN_SEPARATOR = "%>%";

    private final @NotNull List<CallNode> calls;


    public StreamExpressionNode(@NotNull List<CallNode> calls) {
        this.calls = List.copyOf(calls);
    }


    public @NotNull List<CallNode> getCalls() {
        return calls;
    }


    @Override
    public @NotNull String toString() {
        return calls.stream()
                .map(Object::toString)
                .collect(Collectors.joining(CALL_CHAIN_SEPARATOR));
    }

}
