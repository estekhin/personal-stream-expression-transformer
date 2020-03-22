package com.github.estekhin.set.simplify;

import java.util.List;
import java.util.Objects;

import com.github.estekhin.set.ast.BinaryOperationNode;
import com.github.estekhin.set.ast.BooleanNode;
import com.github.estekhin.set.ast.ElementNode;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.NodeVisitor;
import com.github.estekhin.set.ast.NumberNode;
import org.jetbrains.annotations.NotNull;

final class ExpressionVisitor implements NodeVisitor<ExpressionNode> {

    @Override
    public @NotNull ExpressionNode visitNumberNode(@NotNull NumberNode node) {
        return node;
    }

    @Override
    public @NotNull ExpressionNode visitBooleanNode(@NotNull BooleanNode node) {
        return node;
    }

    @Override
    public @NotNull ExpressionNode visitElementNode(@NotNull ElementNode node) {
        return node;
    }

    @Override
    public @NotNull ExpressionNode visitBinaryOperationNode(@NotNull BinaryOperationNode node) {
        ExpressionNode transformedOperand1 = Objects.requireNonNull(node.getOperand1().visit(this));
        ExpressionNode transformedOperand2 = Objects.requireNonNull(node.getOperand2().visit(this));

        List<BinaryOperationTransformer> transformers = List.of(
                new SimpleConstantFoldTransformer(),
                new ZeroConstantTransformer(),
                new OneConstantTransformer(),
                new NegativeConstantTransformer(),
                new BooleanConstantTransformer(),
                new NestedConstantFoldTransformer()
        );
        for (BinaryOperationTransformer transformer : transformers) {
            ExpressionNode simplified = transformer.tryApply(transformedOperand1, node.getOperation(), transformedOperand2);
            if (simplified != null) {
                return simplified;
            }
        }

        return node.updateOperands(transformedOperand1, transformedOperand2);
    }

}
