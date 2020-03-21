package com.github.estekhin.set;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class Cli {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        while (line != null) {
            System.out.println(new StreamExpressionTransformer().transform(line));
            line = reader.readLine();
        }
    }


    private Cli() {
    }

}
