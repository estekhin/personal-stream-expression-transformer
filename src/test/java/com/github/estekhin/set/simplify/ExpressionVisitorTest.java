package com.github.estekhin.set.simplify;

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

class ExpressionVisitorTest {

    @ParameterizedTest
    @MethodSource("expressions")
    void simplifyExpressions(@NotNull ExpressionNode source, @NotNull ExpressionNode expected) {
        assertEquals(expected, source.visit(new ExpressionVisitor()));
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

                // SimpleConstantFoldTransformer
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
                ),
                Arguments.of(
                        new BinaryOperationNode(
                                new NumberNode(2),
                                BinaryOperation.EQUALS,
                                new NumberNode(3)
                        ),
                        new BinaryOperationNode(
                                new NumberNode(1),
                                BinaryOperation.EQUALS,
                                new NumberNode(0)
                        )
                ),
                Arguments.of(
                        new BinaryOperationNode(
                                new NumberNode(2),
                                BinaryOperation.GREATER_THAN,
                                new NumberNode(3)
                        ),
                        new BinaryOperationNode(
                                new NumberNode(1),
                                BinaryOperation.EQUALS,
                                new NumberNode(0)
                        )
                ),
                Arguments.of(
                        new BinaryOperationNode(
                                new NumberNode(2),
                                BinaryOperation.LESS_THAN,
                                new NumberNode(3)
                        ),
                        new BinaryOperationNode(
                                new NumberNode(1),
                                BinaryOperation.EQUALS,
                                new NumberNode(1)
                        )
                ),

                // ZeroConstantTransformer
                Arguments.of(
                        new BinaryOperationNode(
                                new ElementNode(),
                                BinaryOperation.ADD,
                                new NumberNode(0)
                        ),
                        new ElementNode()
                ),
                Arguments.of(
                        new BinaryOperationNode(
                                new ElementNode(),
                                BinaryOperation.SUBTRACT,
                                new NumberNode(0)
                        ),
                        new ElementNode()
                ),
                Arguments.of(
                        new BinaryOperationNode(
                                new ElementNode(),
                                BinaryOperation.MULTIPLY,
                                new NumberNode(0)
                        ),
                        new NumberNode(0)
                ),
                Arguments.of(
                        new BinaryOperationNode(
                                new NumberNode(0),
                                BinaryOperation.ADD,
                                new ElementNode()
                        ),
                        new ElementNode()
                ),
                Arguments.of(
                        new BinaryOperationNode(
                                new NumberNode(0),
                                BinaryOperation.MULTIPLY,
                                new ElementNode()
                        ),
                        new NumberNode(0)
                ),

                // OneConstantTransformer
                Arguments.of(
                        new BinaryOperationNode(
                                new ElementNode(),
                                BinaryOperation.MULTIPLY,
                                new NumberNode(1)
                        ),
                        new ElementNode()
                ),
                Arguments.of(
                        new BinaryOperationNode(
                                new NumberNode(1),
                                BinaryOperation.MULTIPLY,
                                new ElementNode()
                        ),
                        new ElementNode()
                ),

                // NegativeConstantTransformer
                Arguments.of(
                        new BinaryOperationNode(
                                new ElementNode(),
                                BinaryOperation.ADD,
                                new NumberNode(-1)
                        ),
                        new BinaryOperationNode(
                                new ElementNode(),
                                BinaryOperation.SUBTRACT,
                                new NumberNode(1)
                        )
                ),
                Arguments.of(
                        new BinaryOperationNode(
                                new ElementNode(),
                                BinaryOperation.SUBTRACT,
                                new NumberNode(-1)
                        ),
                        new BinaryOperationNode(
                                new ElementNode(),
                                BinaryOperation.ADD,
                                new NumberNode(1)
                        )
                ),
                Arguments.of(
                        new BinaryOperationNode(
                                new NumberNode(-1),
                                BinaryOperation.ADD,
                                new ElementNode()
                        ),
                        new BinaryOperationNode(
                                new ElementNode(),
                                BinaryOperation.SUBTRACT,
                                new NumberNode(1)
                        )
                )
        );
    }

}
