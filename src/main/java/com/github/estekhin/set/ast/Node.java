package com.github.estekhin.set.ast;

import org.jetbrains.annotations.NotNull;

public abstract class Node {

    protected Node() {
    }


    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract @NotNull String toString();

}
