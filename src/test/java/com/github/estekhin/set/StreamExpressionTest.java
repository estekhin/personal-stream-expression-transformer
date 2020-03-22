package com.github.estekhin.set;

import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StreamExpressionTest {

    @ParameterizedTest
    @MethodSource("transformations")
    void transform(@NotNull String sourceExpression, @NotNull String transformedExpression) {
        assertEquals(transformedExpression, StreamExpression.format(StreamExpression.transform(StreamExpression.parse(sourceExpression))));
    }

    @ParameterizedTest
    @MethodSource("transformations")
    void transformAlreadyTransformed(@NotNull String sourceExpression, @NotNull String transformedExpression) {
        assertEquals(transformedExpression, StreamExpression.format(StreamExpression.transform(StreamExpression.parse(transformedExpression))));
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

}