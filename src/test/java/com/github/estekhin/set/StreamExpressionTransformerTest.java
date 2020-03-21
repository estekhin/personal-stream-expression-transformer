package com.github.estekhin.set;

import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StreamExpressionTransformerTest {

    @ParameterizedTest
    @MethodSource("expressions")
    void transform(@NotNull String sourceExpression, @NotNull String transformedExpression) {
        assertEquals(transformedExpression, new StreamExpressionTransformer().transform(sourceExpression));
    }

    @ParameterizedTest
    @MethodSource("expressions")
    void transformAlreadyTransformed(@NotNull String sourceExpression, @NotNull String transformedExpression) {
        assertEquals(transformedExpression, new StreamExpressionTransformer().transform(transformedExpression));
    }

    static @NotNull Stream<Arguments> expressions() {
        return Stream.of(
                Arguments.of(
                        "filter{(element>10)}%>%filter{(element<20)}",
                        "filter{(element>10)&(element<20)}%>%map{element}"
                ),
                Arguments.of(
                        "map{(element+10)}%>%filter{(element>10)}%>%map{(element*element)}",
                        "filter{((element+10)>10)}%>%map{(((element+10)*(element+10)))}"
                ),
                Arguments.of(
                        "map{(element+10)}%>%filter{(element>10)}%>%map{(element*element)}",
                        "filter{(element>0)}%>%map{((element*element)+((element*20)+100))}"
                ),
                Arguments.of(
                        "filter{(element>0)}%>%filter{(element<0)}%>%map{(element*element)}",
                        "filter{(1=0)}%>%map{element}"
                )
        );
    }

}
