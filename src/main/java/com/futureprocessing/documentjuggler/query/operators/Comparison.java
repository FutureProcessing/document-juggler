package com.futureprocessing.documentjuggler.query.operators;

@FunctionalInterface
public interface Comparison<TYPE> {

    void apply(ComparisonOperators<TYPE> comparisonOperators);

}
