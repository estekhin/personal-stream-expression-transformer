package com.github.estekhin.set.simplify;

import java.util.stream.Stream;

import com.github.estekhin.set.ast.ExpressionNode;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static com.github.estekhin.set.ast.BinaryOperation.ADD;
import static com.github.estekhin.set.ast.BinaryOperation.AND;
import static com.github.estekhin.set.ast.BinaryOperation.EQUALS;
import static com.github.estekhin.set.ast.BinaryOperation.GREATER_THAN;
import static com.github.estekhin.set.ast.BinaryOperation.LESS_THAN;
import static com.github.estekhin.set.ast.BinaryOperation.MULTIPLY;
import static com.github.estekhin.set.ast.BinaryOperation.OR;
import static com.github.estekhin.set.ast.BinaryOperation.SUBTRACT;
import static com.github.estekhin.set.ast.Nodes.bool;
import static com.github.estekhin.set.ast.Nodes.element;
import static com.github.estekhin.set.ast.Nodes.number;
import static com.github.estekhin.set.ast.Nodes.op;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ExpressionTransformerTest {

    @ParameterizedTest
    @MethodSource("expressions")
    void simplifyExpressions(@NotNull ExpressionNode source, @NotNull ExpressionNode expected) {
        assertEquals(expected, source.visit(new ExpressionTransformer(DefaultTransformers.transformers)));
    }


    static @NotNull Stream<Arguments> expressions() {
        return Stream.of(
                // SimpleConstantFoldTransformer
                Arguments.of(
                        op(number(2), ADD, number(3)),
                        number(5)
                ),
                Arguments.of(
                        op(number(2), SUBTRACT, number(3)),
                        number(-1)
                ),
                Arguments.of(
                        op(number(2), MULTIPLY, number(3)),
                        number(6)
                ),
                Arguments.of(
                        op(number(2), EQUALS, number(3)),
                        bool(false)
                ),
                Arguments.of(
                        op(number(2), GREATER_THAN, number(3)),
                        bool(false)
                ),
                Arguments.of(
                        op(number(2), LESS_THAN, number(3)),
                        bool(true)
                ),

                // ZeroConstantTransformer
                Arguments.of(
                        op(element(), ADD, number(0)),
                        element()
                ),
                Arguments.of(
                        op(element(), SUBTRACT, number(0)),
                        element()
                ),
                Arguments.of(
                        op(element(), MULTIPLY, number(0)),
                        number(0)
                ),
                Arguments.of(
                        op(number(0), ADD, element()),
                        element()
                ),
                Arguments.of(
                        op(number(0), MULTIPLY, element()),
                        number(0)
                ),

                // OneConstantTransformer
                Arguments.of(
                        op(element(), MULTIPLY, number(1)),
                        element()
                ),
                Arguments.of(
                        op(number(1), MULTIPLY, element()),
                        element()
                ),

                // NegativeConstantTransformer
                Arguments.of(
                        op(element(), ADD, number(-1)),
                        op(element(), SUBTRACT, number(1))
                ),
                Arguments.of(
                        op(element(), SUBTRACT, number(-1)),
                        op(element(), ADD, number(1))
                ),
                Arguments.of(
                        op(number(-1), ADD, element()),
                        op(element(), SUBTRACT, number(1))
                ),
                Arguments.of(
                        op(op(element(), SUBTRACT, op(element(), MULTIPLY, element())), EQUALS, number(-1)),
                        op(number(1), EQUALS, op(op(element(), MULTIPLY, element()), SUBTRACT, element()))
                ),
                Arguments.of(
                        op(number(-1), EQUALS, op(element(), SUBTRACT, op(element(), MULTIPLY, element()))),
                        op(op(op(element(), MULTIPLY, element()), SUBTRACT, element()), EQUALS, number(1))
                ),
                Arguments.of(
                        op(op(element(), SUBTRACT, op(element(), MULTIPLY, element())), GREATER_THAN, number(-1)),
                        op(number(1), GREATER_THAN, op(op(element(), MULTIPLY, element()), SUBTRACT, element()))
                ),
                Arguments.of(
                        op(number(-1), GREATER_THAN, op(element(), SUBTRACT, op(element(), MULTIPLY, element()))),
                        op(op(op(element(), MULTIPLY, element()), SUBTRACT, element()), GREATER_THAN, number(1))
                ),
                Arguments.of(
                        op(op(element(), SUBTRACT, op(element(), MULTIPLY, element())), LESS_THAN, number(-1)),
                        op(number(1), LESS_THAN, op(op(element(), MULTIPLY, element()), SUBTRACT, element()))
                ),
                Arguments.of(
                        op(number(-1), LESS_THAN, op(element(), SUBTRACT, op(element(), MULTIPLY, element()))),
                        op(op(op(element(), MULTIPLY, element()), SUBTRACT, element()), LESS_THAN, number(1))
                ),

                // BooleanConstantTransformer
                Arguments.of(
                        op(bool(true), AND, op(element(), EQUALS, number(0))),
                        op(element(), EQUALS, number(0))
                ),
                Arguments.of(
                        op(bool(false), AND, op(element(), EQUALS, number(0))),
                        bool(false)
                ),
                Arguments.of(
                        op(op(element(), EQUALS, number(0)), AND, bool(true)),
                        op(element(), EQUALS, number(0))
                ),
                Arguments.of(
                        op(op(element(), EQUALS, number(0)), AND, bool(false)),
                        bool(false)
                ),


                Arguments.of(
                        op(bool(true), OR, op(element(), EQUALS, number(0))),
                        bool(true)
                ),
                Arguments.of(
                        op(bool(false), OR, op(element(), EQUALS, number(0))),
                        op(element(), EQUALS, number(0))
                ),
                Arguments.of(
                        op(op(element(), EQUALS, number(0)), OR, bool(true)),
                        bool(true)
                ),
                Arguments.of(
                        op(op(element(), EQUALS, number(0)), OR, bool(false)),
                        op(element(), EQUALS, number(0))
                ),

                // NestedConstantFoldTransformer
                Arguments.of(
                        op(op(element(), ADD, number(1)), ADD, number(2)),
                        op(element(), ADD, number(3))
                ),
                Arguments.of(
                        op(op(element(), ADD, number(1)), SUBTRACT, number(2)),
                        op(element(), ADD, number(-1))
                ),
                Arguments.of(
                        op(op(element(), SUBTRACT, number(1)), ADD, number(2)),
                        op(element(), SUBTRACT, number(-1))
                ),
                Arguments.of(
                        op(op(element(), SUBTRACT, number(1)), SUBTRACT, number(2)),
                        op(element(), SUBTRACT, number(3))
                ),
                Arguments.of(
                        op(op(element(), ADD, number(1)), EQUALS, number(2)),
                        op(element(), EQUALS, number(1))
                ),
                Arguments.of(
                        op(op(element(), SUBTRACT, number(1)), GREATER_THAN, number(2)),
                        op(element(), GREATER_THAN, number(3))
                ),
                Arguments.of(
                        op(op(element(), SUBTRACT, number(1)), LESS_THAN, number(2)),
                        op(element(), LESS_THAN, number(3))
                ),
                Arguments.of(
                        op(number(2), ADD, op(number(1), ADD, element())),
                        op(number(3), ADD, element())
                ),
                Arguments.of(
                        op(number(2), SUBTRACT, op(number(1), ADD, element())),
                        op(number(1), SUBTRACT, element())
                ),
                Arguments.of(
                        op(number(2), ADD, op(number(1), SUBTRACT, element())),
                        op(number(3), SUBTRACT, element())
                ),
                Arguments.of(
                        op(number(2), SUBTRACT, op(number(1), SUBTRACT, element())),
                        op(number(1), ADD, element())
                ),
                Arguments.of(
                        op(number(2), EQUALS, op(number(1), ADD, element())),
                        op(number(1), EQUALS, element())
                ),
                Arguments.of(
                        op(number(2), GREATER_THAN, op(number(1), SUBTRACT, element())),
                        op(element(), GREATER_THAN, number(-1))
                ),
                Arguments.of(
                        op(number(2), LESS_THAN, op(number(1), SUBTRACT, element())),
                        op(element(), LESS_THAN, number(-1))
                ),
                Arguments.of(
                        op(number(2), ADD, op(element(), ADD, number(1))),
                        op(number(3), ADD, element())
                ),
                Arguments.of(
                        op(number(2), SUBTRACT, op(element(), ADD, number(1))),
                        op(number(1), SUBTRACT, element())
                ),
                Arguments.of(
                        op(number(2), ADD, op(element(), SUBTRACT, number(1))),
                        op(number(1), ADD, element())
                ),
                Arguments.of(
                        op(number(2), SUBTRACT, op(element(), SUBTRACT, number(1))),
                        op(number(3), SUBTRACT, element())
                ),
                Arguments.of(
                        op(number(2), EQUALS, op(element(), ADD, number(1))),
                        op(number(1), EQUALS, element())
                ),
                Arguments.of(
                        op(number(2), GREATER_THAN, op(element(), SUBTRACT, number(1))),
                        op(number(3), GREATER_THAN, element())
                ),
                Arguments.of(
                        op(number(2), LESS_THAN, op(element(), SUBTRACT, number(1))),
                        op(number(3), LESS_THAN, element())
                ),
                Arguments.of(
                        op(op(number(1), ADD, element()), ADD, number(2)),
                        op(element(), ADD, number(3))
                ),
                Arguments.of(
                        op(op(number(1), ADD, element()), SUBTRACT, number(2)),
                        op(element(), ADD, number(-1))
                ),
                Arguments.of(
                        op(op(number(1), SUBTRACT, element()), ADD, number(2)),
                        op(number(3), SUBTRACT, element())
                ),
                Arguments.of(
                        op(op(number(1), SUBTRACT, element()), SUBTRACT, number(2)),
                        op(number(-1), SUBTRACT, element())
                ),
                Arguments.of(
                        op(op(number(1), ADD, element()), EQUALS, number(2)),
                        op(element(), EQUALS, number(1))
                ),
                Arguments.of(
                        op(op(number(1), SUBTRACT, element()), GREATER_THAN, number(2)),
                        op(number(-1), GREATER_THAN, element())
                ),
                Arguments.of(
                        op(op(number(1), SUBTRACT, element()), LESS_THAN, number(2)),
                        op(number(-1), LESS_THAN, element())
                ),

                // SameOperandTransformer
                Arguments.of(
                        op(element(), ADD, element()),
                        op(element(), MULTIPLY, number(2))
                ),
                Arguments.of(
                        op(element(), SUBTRACT, element()),
                        number(0)
                ),
                Arguments.of(
                        op(element(), EQUALS, element()),
                        bool(true)
                ),
                Arguments.of(
                        op(element(), GREATER_THAN, element()),
                        bool(false)
                ),
                Arguments.of(
                        op(element(), LESS_THAN, element()),
                        bool(false)
                ),
                Arguments.of(
                        op(op(element(), GREATER_THAN, number(0)), AND, op(element(), GREATER_THAN, number(0))),
                        op(element(), GREATER_THAN, number(0))
                ),
                Arguments.of(
                        op(op(element(), GREATER_THAN, number(0)), OR, op(element(), GREATER_THAN, number(0))),
                        op(element(), GREATER_THAN, number(0))
                ),

                // ElementFirstTransformer
                Arguments.of(
                        op(number(1), ADD, element()),
                        op(element(), ADD, number(1))
                ),
                Arguments.of(
                        op(number(2), MULTIPLY, element()),
                        op(element(), MULTIPLY, number(2))
                ),
                Arguments.of(
                        op(number(2), EQUALS, element()),
                        op(element(), EQUALS, number(2))
                ),
                Arguments.of(
                        op(number(2), GREATER_THAN, element()),
                        op(element(), LESS_THAN, number(2))
                ),
                Arguments.of(
                        op(number(2), LESS_THAN, element()),
                        op(element(), GREATER_THAN, number(2))
                ),

                // BinaryFirstTransformer
                Arguments.of(
                        op(number(1), ADD, op(element(), MULTIPLY, element())),
                        op(op(element(), MULTIPLY, element()), ADD, number(1))
                ),
                Arguments.of(
                        op(element(), ADD, op(element(), MULTIPLY, element())),
                        op(op(element(), MULTIPLY, element()), ADD, element())
                ),

                Arguments.of(
                        op(number(2), MULTIPLY, op(element(), MULTIPLY, element())),
                        op(op(element(), MULTIPLY, element()), MULTIPLY, number(2))
                ),
                Arguments.of(
                        op(element(), MULTIPLY, op(element(), MULTIPLY, element())),
                        op(op(element(), MULTIPLY, element()), MULTIPLY, element())
                ),

                Arguments.of(
                        op(number(1), EQUALS, op(element(), MULTIPLY, element())),
                        op(op(element(), MULTIPLY, element()), EQUALS, number(1))
                ),
                Arguments.of(
                        op(element(), EQUALS, op(element(), MULTIPLY, element())),
                        op(op(element(), MULTIPLY, element()), EQUALS, element())
                ),

                Arguments.of(
                        op(number(1), GREATER_THAN, op(element(), MULTIPLY, element())),
                        op(op(element(), MULTIPLY, element()), LESS_THAN, number(1))
                ),
                Arguments.of(
                        op(element(), GREATER_THAN, op(element(), MULTIPLY, element())),
                        op(op(element(), MULTIPLY, element()), LESS_THAN, element())
                ),

                Arguments.of(
                        op(number(1), LESS_THAN, op(element(), MULTIPLY, element())),
                        op(op(element(), MULTIPLY, element()), GREATER_THAN, number(1))
                ),
                Arguments.of(
                        op(element(), LESS_THAN, op(element(), MULTIPLY, element())),
                        op(op(element(), MULTIPLY, element()), GREATER_THAN, element())
                ),


                // no-op
                Arguments.of(
                        number(1),
                        number(1)
                ),
                Arguments.of(
                        element(),
                        element()
                )
        );
    }

}
