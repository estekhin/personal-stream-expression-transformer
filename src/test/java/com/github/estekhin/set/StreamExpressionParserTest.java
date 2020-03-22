package com.github.estekhin.set;

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
                        new StreamExpressionNode(
                                new FilterCallNode(new BinaryOperationNode(
                                        new NumberNode(1),
                                        BinaryOperation.EQUALS,
                                        new NumberNode(1)
                                ))
                        )
                ),
                Arguments.of(
                        "filter{(1=0)}",
                        new StreamExpressionNode(
                                new FilterCallNode(new BinaryOperationNode(
                                        new NumberNode(1),
                                        BinaryOperation.EQUALS,
                                        new NumberNode(0)
                                ))
                        )
                ),
                Arguments.of(
                        "filter{(1>2)}",
                        new StreamExpressionNode(
                                new FilterCallNode(new BinaryOperationNode(
                                        new NumberNode(1),
                                        BinaryOperation.GREATER_THAN,
                                        new NumberNode(2)
                                ))
                        )
                ),
                Arguments.of(
                        "filter{(1<2)}",
                        new StreamExpressionNode(
                                new FilterCallNode(new BinaryOperationNode(
                                        new NumberNode(1),
                                        BinaryOperation.LESS_THAN,
                                        new NumberNode(2)
                                ))
                        )
                ),
                Arguments.of(
                        "filter{((1<2)&(3>4))}",
                        new StreamExpressionNode(
                                new FilterCallNode(new BinaryOperationNode(
                                        new BinaryOperationNode(
                                                new NumberNode(1),
                                                BinaryOperation.LESS_THAN,
                                                new NumberNode(2)
                                        ),
                                        BinaryOperation.AND,
                                        new BinaryOperationNode(
                                                new NumberNode(3),
                                                BinaryOperation.GREATER_THAN,
                                                new NumberNode(4)
                                        )
                                ))
                        )
                ),
                Arguments.of(
                        "filter{((1<2)|(3>4))}",
                        new StreamExpressionNode(
                                new FilterCallNode(new BinaryOperationNode(
                                        new BinaryOperationNode(
                                                new NumberNode(1),
                                                BinaryOperation.LESS_THAN,
                                                new NumberNode(2)
                                        ),
                                        BinaryOperation.OR,
                                        new BinaryOperationNode(
                                                new NumberNode(3),
                                                BinaryOperation.GREATER_THAN,
                                                new NumberNode(4)
                                        )
                                ))
                        )
                ),
                Arguments.of(
                        "filter{(element>1)}",
                        new StreamExpressionNode(
                                new FilterCallNode(new BinaryOperationNode(
                                        new ElementNode(),
                                        BinaryOperation.GREATER_THAN,
                                        new NumberNode(1)
                                ))
                        )
                ),
                Arguments.of(
                        "filter{(1>element)}",
                        new StreamExpressionNode(
                                new FilterCallNode(new BinaryOperationNode(
                                        new NumberNode(1),
                                        BinaryOperation.GREATER_THAN,
                                        new ElementNode()
                                ))
                        )
                ),
                Arguments.of(
                        "filter{(((element>1)&(element<2))|(element=3))}",
                        new StreamExpressionNode(
                                new FilterCallNode(new BinaryOperationNode(
                                        new BinaryOperationNode(
                                                new BinaryOperationNode(
                                                        new ElementNode(),
                                                        BinaryOperation.GREATER_THAN,
                                                        new NumberNode(1)
                                                ),
                                                BinaryOperation.AND,
                                                new BinaryOperationNode(
                                                        new ElementNode(),
                                                        BinaryOperation.LESS_THAN,
                                                        new NumberNode(2)
                                                )
                                        ),
                                        BinaryOperation.OR,
                                        new BinaryOperationNode(
                                                new ElementNode(),
                                                BinaryOperation.EQUALS,
                                                new NumberNode(3)
                                        )
                                ))
                        )
                ),
                Arguments.of(
                        "filter{(-1>-2)}",
                        new StreamExpressionNode(
                                new FilterCallNode(new BinaryOperationNode(
                                        new NumberNode(-1),
                                        BinaryOperation.GREATER_THAN,
                                        new NumberNode(-2)
                                ))
                        )
                ),

                Arguments.of(
                        "map{1}",
                        new StreamExpressionNode(
                                new MapCallNode(
                                        new NumberNode(1)
                                )
                        )
                ),
                Arguments.of(
                        "map{-1}",
                        new StreamExpressionNode(
                                new MapCallNode(
                                        new NumberNode(-1)
                                )
                        )
                ),
                Arguments.of(
                        "map{(1+2)}",
                        new StreamExpressionNode(
                                new MapCallNode(new BinaryOperationNode(
                                        new NumberNode(1),
                                        BinaryOperation.ADD,
                                        new NumberNode(2)
                                ))
                        )
                ),
                Arguments.of(
                        "map{(1-2)}",
                        new StreamExpressionNode(
                                new MapCallNode(new BinaryOperationNode(
                                        new NumberNode(1),
                                        BinaryOperation.SUBTRACT,
                                        new NumberNode(2)
                                ))
                        )
                ),
                Arguments.of(
                        "map{(1*2)}",
                        new StreamExpressionNode(
                                new MapCallNode(new BinaryOperationNode(
                                        new NumberNode(1),
                                        BinaryOperation.MULTIPLY,
                                        new NumberNode(2)
                                ))
                        )
                ),
                Arguments.of(
                        "map{((1+2)*(3-4))}",
                        new StreamExpressionNode(
                                new MapCallNode(new BinaryOperationNode(
                                        new BinaryOperationNode(
                                                new NumberNode(1),
                                                BinaryOperation.ADD,
                                                new NumberNode(2)
                                        ),
                                        BinaryOperation.MULTIPLY,
                                        new BinaryOperationNode(
                                                new NumberNode(3),
                                                BinaryOperation.SUBTRACT,
                                                new NumberNode(4)
                                        )
                                ))
                        )
                ),
                Arguments.of(
                        "map{element}",
                        new StreamExpressionNode(
                                new MapCallNode(
                                        new ElementNode()
                                )
                        )
                ),
                Arguments.of(
                        "map{(element+1)}",
                        new StreamExpressionNode(
                                new MapCallNode(new BinaryOperationNode(
                                        new ElementNode(),
                                        BinaryOperation.ADD,
                                        new NumberNode(1)
                                ))
                        )
                ),
                Arguments.of(
                        "map{(1+element)}",
                        new StreamExpressionNode(
                                new MapCallNode(new BinaryOperationNode(
                                        new NumberNode(1),
                                        BinaryOperation.ADD,
                                        new ElementNode()
                                ))
                        )
                ),
                Arguments.of(
                        "map{(element--1)}",
                        new StreamExpressionNode(
                                new MapCallNode(new BinaryOperationNode(
                                        new ElementNode(),
                                        BinaryOperation.SUBTRACT,
                                        new NumberNode(-1)
                                ))
                        )
                ),
                Arguments.of(
                        "map{(-1-element)}",
                        new StreamExpressionNode(
                                new MapCallNode(new BinaryOperationNode(
                                        new NumberNode(-1),
                                        BinaryOperation.SUBTRACT,
                                        new ElementNode()
                                ))
                        )
                ),
                Arguments.of(
                        "map{(element+element)}",
                        new StreamExpressionNode(
                                new MapCallNode(new BinaryOperationNode(
                                        new ElementNode(),
                                        BinaryOperation.ADD,
                                        new ElementNode()
                                ))
                        )
                ),
                Arguments.of(
                        "map{((element+(element-1))*(element+element))}",
                        new StreamExpressionNode(
                                new MapCallNode(new BinaryOperationNode(
                                        new BinaryOperationNode(
                                                new ElementNode(),
                                                BinaryOperation.ADD,
                                                new BinaryOperationNode(
                                                        new ElementNode(),
                                                        BinaryOperation.SUBTRACT,
                                                        new NumberNode(1)
                                                )
                                        ),
                                        BinaryOperation.MULTIPLY,
                                        new BinaryOperationNode(
                                                new ElementNode(),
                                                BinaryOperation.ADD,
                                                new ElementNode()
                                        )
                                ))
                        )
                ),
                Arguments.of(
                        "map{(-1+-2)}",
                        new StreamExpressionNode(
                                new MapCallNode(new BinaryOperationNode(
                                        new NumberNode(-1),
                                        BinaryOperation.ADD,
                                        new NumberNode(-2)
                                ))
                        )
                ),
                Arguments.of(
                        "map{(-1--2)}",
                        new StreamExpressionNode(
                                new MapCallNode(new BinaryOperationNode(
                                        new NumberNode(-1),
                                        BinaryOperation.SUBTRACT,
                                        new NumberNode(-2)
                                ))
                        )
                ),
                Arguments.of(
                        "map{(-1*-2)}",
                        new StreamExpressionNode(
                                new MapCallNode(new BinaryOperationNode(
                                        new NumberNode(-1),
                                        BinaryOperation.MULTIPLY,
                                        new NumberNode(-2)
                                ))
                        )
                ),

                Arguments.of(
                        "filter{(element>1)}%>%map{(element+2)}",
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
                                ))
                        )
                ),
                Arguments.of(
                        "map{(element+1)}%>%filter{(element>2)}",
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
                                ))
                        )
                ),

                Arguments.of(
                        "filter{(element>1)}%>%map{(element+2)}%>%filter{(element<3)}%>%map{(element-4)}%>%filter{(element=5)}%>%map{(element*6)}",
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
                                        BinaryOperation.LESS_THAN,
                                        new NumberNode(3)
                                )),
                                new MapCallNode(new BinaryOperationNode(
                                        new ElementNode(),
                                        BinaryOperation.SUBTRACT,
                                        new NumberNode(4)
                                )),
                                new FilterCallNode(new BinaryOperationNode(
                                        new ElementNode(),
                                        BinaryOperation.EQUALS,
                                        new NumberNode(5)
                                )),
                                new MapCallNode(new BinaryOperationNode(
                                        new ElementNode(),
                                        BinaryOperation.MULTIPLY,
                                        new NumberNode(6)
                                ))
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
