package com.github.estekhin.set;

import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StreamExpressionParserTest {

    @ParameterizedTest
    @MethodSource("expressions")
    void parseExpression(@NotNull String source) {
        assertEquals(source, new StreamExpressionParser(source).parse().toString());
    }

    static @NotNull Stream<String> expressions() {
        return Stream.of(
                "filter{(1=1)}",
                "filter{(1=0)}",
                "filter{(1>2)}",
                "filter{(1<2)}",
                "filter{((1<2)&(3>4))}",
                "filter{((1<2)|(3>4))}",
                "filter{(element>1)}",
                "filter{(1>element)}",
                "filter{(((element>1)&(element<2))|(element=3))}",
                "filter{(-1>-2)}",

                "map{1}",
                "map{-1}",
                "map{(1+2)}",
                "map{(1-2)}",
                "map{(1*2)}",
                "map{((1+2)*(3-4))}",
                "map{element}",
                "map{(element+1)}",
                "map{(1+element)}",
                "map{(element--1)}",
                "map{(-1-element)}",
                "map{(element+element)}",
                "map{((element+(element-1))*(element+element))}",
                "map{(-1+-2)}",
                "map{(-1--2)}",

                "filter{(element>1)}%>%map{(element+2)}",
                "map{(element+1)}%>%filter{(element>2)}",

                "filter{(element>1)}%>%map{(element+2)}%>%filter{(element<3)}%>%map{(element-4)}%>%filter{(element=5)}%>%map{(element*6)}"
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
