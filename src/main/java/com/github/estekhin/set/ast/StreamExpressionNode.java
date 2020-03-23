package com.github.estekhin.set.ast;

import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class StreamExpressionNode extends Node {

    static final @NotNull String CALL_CHAIN_SEPARATOR = "%>%";

    private final @NotNull List<CallNode> calls;


    StreamExpressionNode(@NotNull CallNode... calls) {
        this(List.of(calls));
    }

    StreamExpressionNode(@NotNull List<CallNode> calls) {
        if (calls.isEmpty()) {
            throw new IllegalArgumentException("calls is empty");
        }
        this.calls = List.copyOf(calls);
    }


    public @NotNull List<CallNode> getCalls() {
        return calls;
    }

    @Override
    public <R> @Nullable R visit(@NotNull NodeVisitor<R> visitor) {
        return visitor.visitStreamExpressionNode(this);
    }


    @Override
    public int hashCode() {
        return calls.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        StreamExpressionNode other = (StreamExpressionNode) obj;
        return calls.equals(other.calls);
    }

    @Override
    public @NotNull String toString() {
        return calls.stream()
                .map(Object::toString)
                .collect(Collectors.joining(CALL_CHAIN_SEPARATOR));
    }

}
