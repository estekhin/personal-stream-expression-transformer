package com.github.estekhin.set;

import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StreamExpressionTest {

    @ParameterizedTest
    @MethodSource({
            "transformationsFromTaskDescription",
            "rearrangeFormToFilterMap",
            "collectFiltersAndMaps",
            "simpleConstantFold",
            "zeroConstant",
            "oneConstant",
            "negativeConstant",
            "booleanConstant",
            "nestedConstantFold",
            "sameOperand",
            "elementFirst",
    })
    void process(@NotNull String sourceExpression, @NotNull String processedExpression) {
        assertEquals(processedExpression, StreamExpression.process(sourceExpression));
        assertEquals(processedExpression, StreamExpression.process(processedExpression));
    }

    static @NotNull Stream<Arguments> transformationsFromTaskDescription() {
        return Stream.of(
                Arguments.of(
                        "filter{(element>10)}%>%filter{(element<20)}",
                        "filter{((element>10)&(element<20))}%>%map{element}"
                ),
                Arguments.of(
                        "map{(element+10)}%>%filter{(element>10)}%>%map{(element*element)}",
                        "filter{(element>0)}%>%map{((element+10)*(element+10))}"
                ),
                Arguments.of(
                        "filter{(element>0)}%>%filter{(element<0)}%>%map{(element*element)}",
                        "filter{((element>0)&(element<0))}%>%map{(element*element)}"
                )
        );
    }

    static @NotNull Stream<Arguments> rearrangeFormToFilterMap() {
        return Stream.of(
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
                )
        );
    }

    static @NotNull Stream<Arguments> collectFiltersAndMaps() {
        return Stream.of(
                Arguments.of(
                        "filter{(element>0)}%>%filter{((element*element)<100)}",
                        "filter{((element>0)&((element*element)<100))}%>%map{element}"
                ),
                Arguments.of(
                        "map{(element+1)}%>%map{(element*element)}",
                        "filter{(1=1)}%>%map{((element+1)*(element+1))}"
                ),
                Arguments.of(
                        "map{(element*element)}%>%filter{(element>0)}",
                        "filter{((element*element)>0)}%>%map{(element*element)}"
                ),
                Arguments.of(
                        "map{(element+1)}%>%map{(element*element)}%>%filter{(element>0)}",
                        "filter{(((element+1)*(element+1))>0)}%>%map{((element+1)*(element+1))}"
                ),
                Arguments.of(
                        "map{(element*element)}%>%filter{(element>0)}%>%map{(element+1)}",
                        "filter{((element*element)>0)}%>%map{((element*element)+1)}"
                ),
                Arguments.of(
                        "map{(element*element)}%>%filter{(element>0)}%>%filter{((element*element)<100)}",
                        "filter{(((element*element)>0)&(((element*element)*(element*element))<100))}%>%map{(element*element)}"
                )
        );
    }

    static @NotNull Stream<Arguments> simpleConstantFold() {
        return Stream.of(
                Arguments.of(
                        "map{(element+(1+2))}",
                        "filter{(1=1)}%>%map{(element+3)}"
                ),
                Arguments.of(
                        "map{(element+(1-2))}",
                        "filter{(1=1)}%>%map{(element-1)}"
                ),
                Arguments.of(
                        "map{(element+(2*3))}",
                        "filter{(1=1)}%>%map{(element+6)}"
                ),
                Arguments.of(
                        "filter{(1=2)}",
                        "filter{(1=0)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{(1>2)}",
                        "filter{(1=0)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{(1<2)}",
                        "filter{(1=1)}%>%map{element}"
                )
        );
    }

    static @NotNull Stream<Arguments> zeroConstant() {
        return Stream.of(
                Arguments.of(
                        "map{(element+0)}",
                        "filter{(1=1)}%>%map{element}"
                ),
                Arguments.of(
                        "map{(0+element)}",
                        "filter{(1=1)}%>%map{element}"
                ),
                Arguments.of(
                        "map{(element-0)}",
                        "filter{(1=1)}%>%map{element}"
                ),
                Arguments.of(
                        "map{(element*0)}",
                        "filter{(1=1)}%>%map{0}"
                ),
                Arguments.of(
                        "map{(0*element)}",
                        "filter{(1=1)}%>%map{0}"
                )
        );
    }

    static @NotNull Stream<Arguments> oneConstant() {
        return Stream.of(
                Arguments.of(
                        "map{(element*1)}",
                        "filter{(1=1)}%>%map{element}"
                ),
                Arguments.of(
                        "map{(1*element)}",
                        "filter{(1=1)}%>%map{element}"
                )
        );
    }

    static @NotNull Stream<Arguments> negativeConstant() {
        return Stream.of(
                Arguments.of(
                        "map{(element+-1)}",
                        "filter{(1=1)}%>%map{(element-1)}"
                ),
                Arguments.of(
                        "map{(element--1)}",
                        "filter{(1=1)}%>%map{(element+1)}"
                ),
                Arguments.of(
                        "map{(-1+element)}",
                        "filter{(1=1)}%>%map{(element-1)}"
                )
        );
    }

    static @NotNull Stream<Arguments> booleanConstant() {
        return Stream.of(
                Arguments.of(
                        "filter{((element>0)&(1=1))}",
                        "filter{(element>0)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{((1=1)&(element>0))}",
                        "filter{(element>0)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{((element>0)&(1=0))}",
                        "filter{(1=0)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{((1=0)&(element>0))}",
                        "filter{(1=0)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{((element>0)|(1=1))}",
                        "filter{(1=1)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{((1=1)|(element>0))}",
                        "filter{(1=1)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{((element>0)|(1=0))}",
                        "filter{(element>0)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{((1=0)|(element>0))}",
                        "filter{(element>0)}%>%map{element}"
                )
        );
    }

    static @NotNull Stream<Arguments> nestedConstantFold() {
        return Stream.of(
                Arguments.of(
                        "map{((element+1)+2)}",
                        "filter{(1=1)}%>%map{(element+3)}"
                ),
                Arguments.of(
                        "map{((1+element)+2)}",
                        "filter{(1=1)}%>%map{(element+3)}"
                ),
                Arguments.of(
                        "map{(1+(element+2))}",
                        "filter{(1=1)}%>%map{(element+3)}"
                ),
                Arguments.of(
                        "map{(1+(2+element))}",
                        "filter{(1=1)}%>%map{(element+3)}"
                ),

                Arguments.of(
                        "map{((element+1)-2)}",
                        "filter{(1=1)}%>%map{(element-1)}"
                ),
                Arguments.of(
                        "map{((1+element)-2)}",
                        "filter{(1=1)}%>%map{(element-1)}"
                ),
                Arguments.of(
                        "map{(1+(element-2))}",
                        "filter{(1=1)}%>%map{(element-1)}"
                ),
                Arguments.of(
                        "map{(1+(2-element))}",
                        "filter{(1=1)}%>%map{(3-element)}"
                ),

                Arguments.of(
                        "map{((element-1)+2)}",
                        "filter{(1=1)}%>%map{(element+1)}"
                ),
                Arguments.of(
                        "map{((1-element)+2)}",
                        "filter{(1=1)}%>%map{(3-element)}"
                ),
                Arguments.of(
                        "map{(1-(element+2))}",
                        "filter{(1=1)}%>%map{(-1-element)}"
                ),
                Arguments.of(
                        "map{(1-(2+element))}",
                        "filter{(1=1)}%>%map{(-1-element)}"
                ),

                Arguments.of(
                        "map{((element-1)-2)}",
                        "filter{(1=1)}%>%map{(element-3)}"
                ),
                Arguments.of(
                        "map{((1-element)-2)}",
                        "filter{(1=1)}%>%map{(-1-element)}"
                ),
                Arguments.of(
                        "map{(1-(element-2))}",
                        "filter{(1=1)}%>%map{(3-element)}"
                ),
                Arguments.of(
                        "map{(1-(2-element))}",
                        "filter{(1=1)}%>%map{(element-1)}"
                ),

                Arguments.of(
                        "filter{((element+1)=2)}",
                        "filter{(element=1)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{((1+element)=2)}",
                        "filter{(element=1)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{(2=(element+1))}",
                        "filter{(element=1)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{(2=(1+element))}",
                        "filter{(element=1)}%>%map{element}"
                ),

                Arguments.of(
                        "filter{((element-1)=2)}",
                        "filter{(element=3)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{((1-element)=2)}",
                        "filter{(element=-1)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{(2=(element-1))}",
                        "filter{(element=3)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{(2=(1-element))}",
                        "filter{(element=-1)}%>%map{element}"
                ),

                Arguments.of(
                        "filter{((element+1)>2)}",
                        "filter{(element>1)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{((1+element)>2)}",
                        "filter{(element>1)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{(2>(element+1))}",
                        "filter{(element<1)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{(2>(1+element))}",
                        "filter{(element<1)}%>%map{element}"
                ),

                Arguments.of(
                        "filter{((element-1)>2)}",
                        "filter{(element>3)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{((1-element)>2)}",
                        "filter{(element<-1)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{(2>(element-1))}",
                        "filter{(element<3)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{(2>(1-element))}",
                        "filter{(element>-1)}%>%map{element}"
                ),

                Arguments.of(
                        "filter{((element+1)<2)}",
                        "filter{(element<1)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{((1+element)<2)}",
                        "filter{(element<1)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{(2<(element+1))}",
                        "filter{(element>1)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{(2<(1+element))}",
                        "filter{(element>1)}%>%map{element}"
                ),

                Arguments.of(
                        "filter{((element-1)<2)}",
                        "filter{(element<3)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{((1-element)<2)}",
                        "filter{(element>-1)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{(2<(element-1))}",
                        "filter{(element>3)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{(2<(1-element))}",
                        "filter{(element<-1)}%>%map{element}"
                )
        );
    }

    static @NotNull Stream<Arguments> sameOperand() {
        return Stream.of(
                Arguments.of(
                        "map{(element+element)}",
                        "filter{(1=1)}%>%map{(element*2)}"
                ),
                Arguments.of(
                        "map{(element-element)}",
                        "filter{(1=1)}%>%map{0}"
                ),
                Arguments.of(
                        "filter{(element=element)}",
                        "filter{(1=1)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{(element>element)}",
                        "filter{(1=0)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{(element<element)}",
                        "filter{(1=0)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{((element>0)&(element>0))}",
                        "filter{(element>0)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{((element>0)|(element>0))}",
                        "filter{(element>0)}%>%map{element}"
                )
        );
    }

    static @NotNull Stream<Arguments> elementFirst() {
        return Stream.of(
                Arguments.of(
                        "map{(1+element)}",
                        "filter{(1=1)}%>%map{(element+1)}"
                ),
                Arguments.of(
                        "map{(2*element)}",
                        "filter{(1=1)}%>%map{(element*2)}"
                ),
                Arguments.of(
                        "filter{(1=element)}",
                        "filter{(element=1)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{(1<element)}",
                        "filter{(element>1)}%>%map{element}"
                ),
                Arguments.of(
                        "filter{(1>element)}",
                        "filter{(element<1)}%>%map{element}"
                )
        );
    }

}
