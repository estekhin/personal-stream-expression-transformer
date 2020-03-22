package com.github.estekhin.set;

import java.util.Objects;

import com.github.estekhin.set.ast.StreamExpressionNode;
import com.github.estekhin.set.ast.StreamExpressionParser;
import com.github.estekhin.set.form.FormStreamExpressionTransformer;
import com.github.estekhin.set.simplify.SimplifyStreamExpressionTransformer;
import org.jetbrains.annotations.NotNull;

public final class StreamExpression {

    public static @NotNull StreamExpressionNode parse(@NotNull String source) {
        return new StreamExpressionParser(source).parse();
    }

    public static @NotNull String format(@NotNull StreamExpressionNode node) {
        return node.toString();
    }


    public static @NotNull StreamExpressionNode transform(@NotNull StreamExpressionNode node) {
        return Objects.requireNonNull(node.visit(new FormStreamExpressionTransformer()));
    }

    public static @NotNull StreamExpressionNode simplify(@NotNull StreamExpressionNode node) {
        return Objects.requireNonNull(node.visit(new SimplifyStreamExpressionTransformer()));
    }


    public static @NotNull String process(@NotNull String source) {
        StreamExpressionNode sourceExpression = parse(source);
        StreamExpressionNode transformedExpression = transform(sourceExpression);
        StreamExpressionNode simplifiedExpression = simplify(transformedExpression);
        return format(simplifiedExpression);
    }


    private StreamExpression() {
    }

}
