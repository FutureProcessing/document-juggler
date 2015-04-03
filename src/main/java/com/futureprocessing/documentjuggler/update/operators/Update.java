package com.futureprocessing.documentjuggler.update.operators;

@FunctionalInterface
public interface Update<TYPE> {

    void apply(UpdateOperators<TYPE> updateOperators);
}
