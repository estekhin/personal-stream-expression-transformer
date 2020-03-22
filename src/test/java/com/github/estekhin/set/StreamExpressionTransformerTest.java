package com.github.estekhin.set;

import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StreamExpressionTransformerTest {

    @ParameterizedTest
    @MethodSource("transformations")
    void transform(@NotNull String sourceExpression, @NotNull String transformedExpression) {
        assertEquals(transformedExpression, new StreamExpressionTransformer().transform(sourceExpression));
    }

    @ParameterizedTest
    @MethodSource("transformations")
    void transformAlreadyTransformed(@NotNull String sourceExpression, @NotNull String transformedExpression) {
        assertEquals(transformedExpression, new StreamExpressionTransformer().transform(transformedExpression));
    }

    static @NotNull Stream<Arguments> transformations() {
        return Stream.of(
                Arguments.of(
                        "filter{(element>10)}%>%filter{(element<20)}",
                        "filter{((element>10)&(element<20))}%>%map{element}"
                ),
                Arguments.of(
                        "map{(element+10)}%>%filter{(element>10)}%>%map{(element*element)}",
                        "filter{((element+10)>10)}%>%map{((element+10)*(element+10))}"
                ),
                Arguments.of(
                        "filter{(element>0)}%>%filter{(element<0)}%>%map{(element*element)}",
                        "filter{((element>0)&(element<0))}%>%map{(element*element)}"
                ),
                Arguments.of(
                        "filter{(1=1)}",
                        "filter{(1=1)}%>%map{element}"
                ),
                Arguments.of(
                        "map{element}",
                        "filter{(1=1)}%>%map{element}"
                ),
                Arguments.of(
                        "map{element}%>%filter{(1=1)}",
                        "filter{(1=1)}%>%map{element}"
                ),
                Arguments.of(
                        "map{(element+1)}%>%map{(element+2)}%>%filter{(element>3)}",
                        "filter{(((element+1)+2)>3)}%>%map{((element+1)+2)}"
                ),
                Arguments.of(
                        "map{(element+1)}%>%filter{(element>1)}%>%filter{(element>2)}",
                        "filter{(((element+1)>1)&((element+1)>2))}%>%map{(element+1)}"
                ),
                Arguments.of(
                        "map{(element+1)}%>%filter{(((element>1)|(element<2))&((element*element)=25))}",
                        "filter{((((element+1)>1)|((element+1)<2))&(((element+1)*(element+1))=25))}%>%map{(element+1)}"
                )
        );
    }


    @ParameterizedTest
    @MethodSource("expressionsWithSyntaxErrors")
    void transformExpressionWithSyntaxErrorThrowsExpressionSyntaxException(@NotNull String expression) {
        assertThrows(ExpressionSyntaxException.class, () -> new StreamExpressionTransformer().transform(expression));
    }

    static @NotNull Stream<String> expressionsWithSyntaxErrors() {
        return Stream.of(
                "map{element}>>>map{element}",
                "map",
                "filter",
                "call{}",
                "map{element",
                "map{element)",
                "map{element1}",
                "map{item}",
                "map{+1}",
                "map{1e}",
                "map{--1}",
                "map{(1+2}}",
                "map{(1^2)}",
                "map{(1+2+3)}"
        );
    }

    @ParameterizedTest
    @MethodSource("expressionsWithTypeErrors")
    void transformExpressionWithTypeErrorThrowsExpressionTypeException(@NotNull String expression) {
        assertThrows(ExpressionTypeException.class, () -> new StreamExpressionTransformer().transform(expression));
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
