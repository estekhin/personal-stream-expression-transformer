package com.github.estekhin.set.parser;

import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

public final class CallChainNode extends Node {

    private final @NotNull List<CallNode> calls;


    public CallChainNode(@NotNull List<CallNode> calls) {
        this.calls = List.copyOf(calls);
    }


    public @NotNull List<CallNode> getCalls() {
        return calls;
    }

    @Override
    public @NotNull String toString() {
        return calls.stream()
                .map(Object::toString)
                .collect(Collectors.joining(Tokens.CALL_CHAIN_SEPARATOR));
    }

}
