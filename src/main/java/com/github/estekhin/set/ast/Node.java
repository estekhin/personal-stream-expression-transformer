package com.github.estekhin.set.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Node {

    protected Node() {
    }


    public abstract <R> @Nullable R visit(@NotNull NodeVisitor<R> visitor);


    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract @NotNull String toString();

}
