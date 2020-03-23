package com.github.estekhin.set.simplify;

import java.util.List;

final class DefaultTransformers {

    public static final List<BinaryOperationTransformer> transformers = List.of(
            new SimpleConstantFoldTransformer(),
            new ZeroConstantTransformer(),
            new OneConstantTransformer(),
            new NegativeConstantTransformer(),
            new BooleanConstantTransformer(),
            new NestedConstantFoldTransformer(),
            new ElementFirstTransformer()
    );


    private DefaultTransformers() {
    }

}
