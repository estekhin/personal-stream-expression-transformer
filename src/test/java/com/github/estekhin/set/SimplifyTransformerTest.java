package com.github.estekhin.set;

import java.util.stream.Stream;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.BinaryOperationNode;
import com.github.estekhin.set.ast.ElementNode;
import com.github.estekhin.set.ast.ExpressionNode;
import com.github.estekhin.set.ast.NumberNode;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimplifyTransformerTest {

    @ParameterizedTest
    @MethodSource("expressions")
    void simplifyExpressions(@NotNull ExpressionNode source, @NotNull ExpressionNode expected) {
        assertEquals(expected, source.visit(new SimplifyTransformer.ExpressionVisitor()));
    }


    static @NotNull Stream<Arguments> expressions() {
        return Stream.of(
                Arguments.of(
                        new NumberNode(1),
                        new NumberNode(1)
                ),
                Arguments.of(
                        new ElementNode(),
                        new ElementNode()
                ),

                Arguments.of(
                        new BinaryOperationNode(
                                new NumberNode(2),
                                BinaryOperation.ADD,
                                new NumberNode(3)
                        ),
                        new NumberNode(5)
                ),
                Arguments.of(
                        new BinaryOperationNode(
                                new NumberNode(2),
                                BinaryOperation.SUBTRACT,
                                new NumberNode(3)
                        ),
                        new NumberNode(-1)
                ),
                Arguments.of(
                        new BinaryOperationNode(
                                new NumberNode(2),
                                BinaryOperation.MULTIPLY,
                                new NumberNode(3)
                        ),
                        new NumberNode(6)
                )
        );
    }

}
