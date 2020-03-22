package com.github.estekhin.set;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.github.estekhin.set.ast.StreamExpressionNode;

public final class Cli {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        while (line != null) {
            try {
                StreamExpressionNode sourceExpression = StreamExpression.parse(line);
                StreamExpressionNode transformedExpression = StreamExpression.transform(sourceExpression);
                StreamExpressionNode simplifiedExpression = StreamExpression.simplify(transformedExpression);
                System.out.println(StreamExpression.format(simplifiedExpression));
            } catch (ExpressionSyntaxException ignored) {
                System.out.println("SYNTAX ERROR");
            } catch (ExpressionTypeException ignored) {
                System.out.println("TYPE ERROR");
            }
            line = reader.readLine();
        }
    }


    private Cli() {
    }

}
