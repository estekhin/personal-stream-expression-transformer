package com.github.estekhin.set.ast;

import java.util.stream.Stream;

import com.github.estekhin.set.ExpressionSyntaxException;
import com.github.estekhin.set.ExpressionTypeException;
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
import static com.github.estekhin.set.ast.Nodes.expression;
import static com.github.estekhin.set.ast.Nodes.filter;
import static com.github.estekhin.set.ast.Nodes.map;
import static com.github.estekhin.set.ast.Nodes.number;
import static com.github.estekhin.set.ast.Nodes.op;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StreamExpressionParserTest {

    @ParameterizedTest
    @MethodSource("expressions")
    void parseExpression(@NotNull String source, @NotNull StreamExpressionNode expression) {
        assertEquals(expression, new StreamExpressionParser(source).parse());
    }

    @ParameterizedTest
    @MethodSource("expressions")
    void formatExpression(@NotNull String source, @NotNull StreamExpressionNode expression) {
        assertEquals(source, expression.toString());
    }

    static @NotNull Stream<Arguments> expressions() {
        return Stream.of(
                Arguments.of(
                        "filter{(1=1)}",
                        expression(
                                filter(bool(true))
                        )
                ),
                Arguments.of(
                        "filter{(1=0)}",
                        expression(
                                filter(bool(false))
                        )
                ),
                Arguments.of(
                        "filter{(1>2)}",
                        expression(
                                filter(op(number(1), GREATER_THAN, number(2)))
                        )
                ),
                Arguments.of(
                        "filter{(1<2)}",
                        expression(
                                filter(op(number(1), LESS_THAN, number(2)))
                        )
                ),
                Arguments.of(
                        "filter{((1<2)&(3>4))}",
                        expression(
                                filter(op(op(number(1), LESS_THAN, number(2)), AND, op(number(3), GREATER_THAN, number(4))))
                        )
                ),
                Arguments.of(
                        "filter{((1<2)|(3>4))}",
                        expression(
                                filter(op(op(number(1), LESS_THAN, number(2)), OR, op(number(3), GREATER_THAN, number(4))))
                        )
                ),
                Arguments.of(
                        "filter{(element>1)}",
                        expression(
                                filter(op(element(), GREATER_THAN, number(1)))
                        )
                ),
                Arguments.of(
                        "filter{(1>element)}",
                        expression(
                                filter(op(number(1), GREATER_THAN, element()))
                        )
                ),
                Arguments.of(
                        "filter{(((element>1)&(element<2))|(element=3))}",
                        expression(
                                filter(op(op(op(element(), GREATER_THAN, number(1)), AND, op(element(), LESS_THAN, number(2))), OR, op(element(), EQUALS, number(3))))
                        )
                ),
                Arguments.of(
                        "filter{(-1>-2)}",
                        expression(
                                filter(op(number(-1), GREATER_THAN, number(-2)))
                        )
                ),

                Arguments.of(
                        "map{1}",
                        expression(
                                map(number(1))
                        )
                ),
                Arguments.of(
                        "map{-1}",
                        expression(
                                map(number(-1))
                        )
                ),
                Arguments.of(
                        "map{(1+2)}",
                        expression(
                                map(op(number(1), ADD, number(2)))
                        )
                ),
                Arguments.of(
                        "map{(1-2)}",
                        expression(
                                map(op(number(1), SUBTRACT, number(2)))
                        )
                ),
                Arguments.of(
                        "map{(1*2)}",
                        expression(
                                map(op(number(1), MULTIPLY, number(2)))
                        )
                ),
                Arguments.of(
                        "map{((1+2)*(3-4))}",
                        expression(
                                map(op(op(number(1), ADD, number(2)), MULTIPLY, op(number(3), SUBTRACT, number(4))))
                        )
                ),
                Arguments.of(
                        "map{element}",
                        expression(
                                map(element())
                        )
                ),
                Arguments.of(
                        "map{(element+1)}",
                        expression(
                                map(op(element(), ADD, number(1)))
                        )
                ),
                Arguments.of(
                        "map{(1+element)}",
                        expression(
                                map(op(number(1), ADD, element()))
                        )
                ),
                Arguments.of(
                        "map{(element--1)}",
                        expression(
                                map(op(element(), SUBTRACT, number(-1)))
                        )
                ),
                Arguments.of(
                        "map{(-1-element)}",
                        expression(
                                map(op(number(-1), SUBTRACT, element()))
                        )
                ),
                Arguments.of(
                        "map{(element+element)}",
                        expression(
                                map(op(element(), ADD, element()))
                        )
                ),
                Arguments.of(
                        "map{((element+(element-1))*(element+element))}",
                        expression(
                                map(op(op(element(), ADD, op(element(), SUBTRACT, number(1))), MULTIPLY, op(element(), ADD, element())))
                        )
                ),
                Arguments.of(
                        "map{(-1+-2)}",
                        expression(
                                map(op(number(-1), ADD, number(-2)))
                        )
                ),
                Arguments.of(
                        "map{(-1--2)}",
                        expression(
                                map(op(number(-1), SUBTRACT, number(-2)))
                        )
                ),
                Arguments.of(
                        "map{(-1*-2)}",
                        expression(
                                map(op(number(-1), MULTIPLY, number(-2)))
                        )
                ),

                Arguments.of(
                        "filter{(element>1)}%>%map{(element+2)}",
                        expression(
                                filter(op(element(), GREATER_THAN, number(1))),
                                map(op(element(), ADD, number(2)))
                        )
                ),
                Arguments.of(
                        "map{(element+1)}%>%filter{(element>2)}",
                        expression(
                                map(op(element(), ADD, number(1))),
                                filter(op(element(), GREATER_THAN, number(2)))
                        )
                ),

                Arguments.of(
                        "filter{(element>1)}%>%map{(element+2)}%>%filter{(element<3)}%>%map{(element-4)}%>%filter{(element=5)}%>%map{(element*6)}",
                        expression(
                                filter(op(element(), GREATER_THAN, number(1))),
                                map(op(element(), ADD, number(2))),
                                filter(op(element(), LESS_THAN, number(3))),
                                map(op(element(), SUBTRACT, number(4))),
                                filter(op(element(), EQUALS, number(5))),
                                map(op(element(), MULTIPLY, number(6)))
                        )
                )
        );
    }


    @ParameterizedTest
    @MethodSource("expressionsWithSyntaxErrors")
    void parseExpressionWithSyntaxErrorThrowsExpressionSyntaxException(@NotNull String source) {
        assertThrows(ExpressionSyntaxException.class, () ->
                new StreamExpressionParser(source).parse()
        );
    }

    static @NotNull Stream<String> expressionsWithSyntaxErrors() {
        return Stream.of(
                "",
                "map{element}>>>map{element}",
                "map{element}%>%",
                "map",
                "filter",
                "map()",
                "filter()",
                "call{}",
                "map{element",
                "map{element)",
                "map{element1}",
                "map{item}",
                "map{+1}",
                "map{1element}",
                "map{()}",
                "map{--1}",
                "map{(1+2}}",
                "map{(1^2)}",
                "map{(1+2+3)}",
                "map{((1+2))}"
        );
    }


    @ParameterizedTest
    @MethodSource("expressionsWithTypeErrors")
    void parseExpressionWithTypeErrorThrowsExpressionTypeException(@NotNull String source) {
        assertThrows(ExpressionTypeException.class, () ->
                new StreamExpressionParser(source).parse()
        );
    }

    static @NotNull Stream<String> expressionsWithTypeErrors() {
        return Stream.of(
                "map{(1>2)}",
                "filter{element}",
                "filter{1}",
                "filter{(1+2)}",
                "map{(1+(2>3))}",
                "map{((1>2)+3))}",
                "filter{(1>(2>3))}",
                "filter{((1>2)>3)}"
        );
    }

}
