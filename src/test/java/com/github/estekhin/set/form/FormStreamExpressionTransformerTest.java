package com.github.estekhin.set.form;

import java.util.stream.Stream;

import com.github.estekhin.set.ast.StreamExpressionNode;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static com.github.estekhin.set.ast.BinaryOperation.ADD;
import static com.github.estekhin.set.ast.BinaryOperation.AND;
import static com.github.estekhin.set.ast.BinaryOperation.EQUALS;
import static com.github.estekhin.set.ast.BinaryOperation.GREATER_THAN;
import static com.github.estekhin.set.ast.Nodes.element;
import static com.github.estekhin.set.ast.Nodes.expression;
import static com.github.estekhin.set.ast.Nodes.filter;
import static com.github.estekhin.set.ast.Nodes.map;
import static com.github.estekhin.set.ast.Nodes.number;
import static com.github.estekhin.set.ast.Nodes.op;
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
                        expression(
                                filter(op(number(1), EQUALS, number(1)))
                        ),
                        expression(
                                filter(op(number(1), EQUALS, number(1))),
                                map(element())
                        )
                ),
                Arguments.of(
                        expression(
                                map(element())
                        ),
                        expression(
                                filter(op(number(1), EQUALS, number(1))),
                                map(element())
                        )
                ),
                Arguments.of(
                        expression(
                                map(element()),
                                filter(op(number(1), EQUALS, number(1)))
                        ),
                        expression(
                                filter(op(number(1), EQUALS, number(1))),
                                map(element())
                        )
                ),
                Arguments.of(
                        expression(
                                filter(op(element(), GREATER_THAN, number(1))),
                                filter(op(element(), GREATER_THAN, number(2)))
                        ),
                        expression(
                                filter(op(op(element(), GREATER_THAN, number(1)), AND, op(element(), GREATER_THAN, number(2)))),
                                map(element())
                        )
                ),
                Arguments.of(
                        expression(
                                filter(op(element(), GREATER_THAN, number(1))),
                                map(op(element(), ADD, number(2))),
                                filter(op(element(), GREATER_THAN, number(3)))
                        ),
                        expression(
                                filter(op(op(element(), GREATER_THAN, number(1)), AND, op(op(element(), ADD, number(2)), GREATER_THAN, number(3)))),
                                map(op(element(), ADD, number(2)))
                        )
                ),
                Arguments.of(
                        expression(
                                map(op(element(), ADD, number(1))),
                                filter(op(element(), GREATER_THAN, number(2))),
                                map(op(element(), ADD, number(3))),
                                filter(op(element(), GREATER_THAN, number(4)))
                        ),
                        expression(
                                filter(op(op(op(element(), ADD, number(1)), GREATER_THAN, number(2)), AND, op(op(op(element(), ADD, number(1)), ADD, number(3)), GREATER_THAN, number(4)))),
                                map(op(op(element(), ADD, number(1)), ADD, number(3)))
                        )
                )
        );
    }

}
