package com.github.estekhin.set.form;

import java.util.stream.Stream;

import com.github.estekhin.set.ast.BinaryOperation;
import com.github.estekhin.set.ast.BinaryOperationNode;
import com.github.estekhin.set.ast.ElementNode;
import com.github.estekhin.set.ast.FilterCallNode;
import com.github.estekhin.set.ast.MapCallNode;
import com.github.estekhin.set.ast.NumberNode;
import com.github.estekhin.set.ast.StreamExpressionNode;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FormStreamExpressionTransformerTest {

    @ParameterizedTest
    @MethodSource("transformations")
    void transformToFilterMapForm(@NotNull StreamExpressionNode source, @NotNull StreamExpressionNode expected) {
        assertEquals(expected, source.visit(new FormStreamExpressionTransformer()));
    }

    static @NotNull Stream<Arguments> transformations() {
        return Stream.of(
                Arguments.of(
                        new StreamExpressionNode(
                                new FilterCallNode(new BinaryOperationNode(
                                        new NumberNode(1),
                                        BinaryOperation.EQUALS,
                                        new NumberNode(1)
                                ))
                        ),
                        new StreamExpressionNode(
                                new FilterCallNode(new BinaryOperationNode(
                                        new NumberNode(1),
                                        BinaryOperation.EQUALS,
                                        new NumberNode(1)
                                )),
                                new MapCallNode(
                                        new ElementNode()
                                )
                        )
                ),
                Arguments.of(
                        new StreamExpressionNode(
                                new MapCallNode(
                                        new ElementNode()
                                )
                        ),
                        new StreamExpressionNode(
                                new FilterCallNode(new BinaryOperationNode(
                                        new NumberNode(1),
                                        BinaryOperation.EQUALS,
                                        new NumberNode(1)
                                )),
                                new MapCallNode(
                                        new ElementNode()
                                )
                        )
                ),
                Arguments.of(
                        new StreamExpressionNode(
                                new MapCallNode(
                                        new ElementNode()
                                ),
                                new FilterCallNode(new BinaryOperationNode(
                                        new NumberNode(1),
                                        BinaryOperation.EQUALS,
                                        new NumberNode(1)
                                ))
                        ),
                        new StreamExpressionNode(
                                new FilterCallNode(new BinaryOperationNode(
                                        new NumberNode(1),
                                        BinaryOperation.EQUALS,
                                        new NumberNode(1)
                                )),
                                new MapCallNode(
                                        new ElementNode()
                                )
                        )
                ),
                Arguments.of(
                        new StreamExpressionNode(
                                new FilterCallNode(new BinaryOperationNode(
                                        new ElementNode(),
                                        BinaryOperation.GREATER_THAN,
                                        new NumberNode(1)
                                )),
                                new FilterCallNode(new BinaryOperationNode(
                                        new ElementNode(),
                                        BinaryOperation.GREATER_THAN,
                                        new NumberNode(2)
                                ))
                        ),
                        new StreamExpressionNode(
                                new FilterCallNode(new BinaryOperationNode(
                                        new BinaryOperationNode(
                                                new ElementNode(),
                                                BinaryOperation.GREATER_THAN,
                                                new NumberNode(1)
                                        ),
                                        BinaryOperation.AND,
                                        new BinaryOperationNode(
                                                new ElementNode(),
                                                BinaryOperation.GREATER_THAN,
                                                new NumberNode(2)
                                        )
                                )),
                                new MapCallNode(
                                        new ElementNode()
                                )
                        )
                ),
                Arguments.of(
                        new StreamExpressionNode(
                                new FilterCallNode(new BinaryOperationNode(
                                        new ElementNode(),
                                        BinaryOperation.GREATER_THAN,
                                        new NumberNode(1)
                                )),
                                new MapCallNode(new BinaryOperationNode(
                                        new ElementNode(),
                                        BinaryOperation.ADD,
                                        new NumberNode(2)
                                )),
                                new FilterCallNode(new BinaryOperationNode(
                                        new ElementNode(),
                                        BinaryOperation.GREATER_THAN,
                                        new NumberNode(3)
                                ))
                        ),
                        new StreamExpressionNode(
                                new FilterCallNode(new BinaryOperationNode(
                                        new BinaryOperationNode(
                                                new ElementNode(),
                                                BinaryOperation.GREATER_THAN,
                                                new NumberNode(1)
                                        ),
                                        BinaryOperation.AND,
                                        new BinaryOperationNode(
                                                new BinaryOperationNode(
                                                        new ElementNode(),
                                                        BinaryOperation.ADD,
                                                        new NumberNode(2)
                                                ),
                                                BinaryOperation.GREATER_THAN,
                                                new NumberNode(3)
                                        )
                                )),
                                new MapCallNode(new BinaryOperationNode(
                                        new ElementNode(),
                                        BinaryOperation.ADD,
                                        new NumberNode(2)
                                ))
                        )
                ),
                Arguments.of(
                        new StreamExpressionNode(
                                new MapCallNode(new BinaryOperationNode(
                                        new ElementNode(),
                                        BinaryOperation.ADD,
                                        new NumberNode(1)
                                )),
                                new FilterCallNode(new BinaryOperationNode(
                                        new ElementNode(),
                                        BinaryOperation.GREATER_THAN,
                                        new NumberNode(2)
                                )),
                                new MapCallNode(new BinaryOperationNode(
                                        new ElementNode(),
                                        BinaryOperation.ADD,
                                        new NumberNode(3)
                                )),
                                new FilterCallNode(new BinaryOperationNode(
                                        new ElementNode(),
                                        BinaryOperation.GREATER_THAN,
                                        new NumberNode(4)
                                ))
                        ),
                        new StreamExpressionNode(
                                new FilterCallNode(new BinaryOperationNode(
                                        new BinaryOperationNode(
                                                new BinaryOperationNode(
                                                        new ElementNode(),
                                                        BinaryOperation.ADD,
                                                        new NumberNode(1)
                                                ),
                                                BinaryOperation.GREATER_THAN,
                                                new NumberNode(2)
                                        ),
                                        BinaryOperation.AND,
                                        new BinaryOperationNode(
                                                new BinaryOperationNode(
                                                        new BinaryOperationNode(
                                                                new ElementNode(),
                                                                BinaryOperation.ADD,
                                                                new NumberNode(1)
                                                        ),
                                                        BinaryOperation.ADD,
                                                        new NumberNode(3)
                                                ),
                                                BinaryOperation.GREATER_THAN,
                                                new NumberNode(4)
                                        )
                                )),
                                new MapCallNode(new BinaryOperationNode(
                                        new BinaryOperationNode(
                                                new ElementNode(),
                                                BinaryOperation.ADD,
                                                new NumberNode(1)
                                        ),
                                        BinaryOperation.ADD,
                                        new NumberNode(3)
                                ))
                        )
                )
        );
    }

}
