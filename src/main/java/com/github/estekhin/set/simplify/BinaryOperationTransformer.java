package com.github.estekhin.set.simplify;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.ExpressionNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

interface BinaryOperationTransformer {

    @Nullable ExpressionNode tryApply(@NotNull ExpressionNode operand1, @NotNull BinaryOperation operation, @NotNull ExpressionNode operand2);

}
