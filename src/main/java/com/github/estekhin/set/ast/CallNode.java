package com.github.estekhin.set.ast;

import org.jetbrains.annotations.NotNull;

public abstract class CallNode extends Node {

    protected final @NotNull ExpressionNode operand;


    protected CallNode(@NotNull ExpressionNode operand) {
        this.operand = operand;
    }


    public final @NotNull ExpressionNode getOperand() {
        return operand;
    }

    public abstract void visit(@NotNull CallNodeVisitor visitor);


    @Override
    public int hashCode() {
        // todo: hashCode does not use node type (filter or map), but should
        return operand.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        CallNode other = (CallNode) obj;
        return operand.equals(other.operand);
    }

}
