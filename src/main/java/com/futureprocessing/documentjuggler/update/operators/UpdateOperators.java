package com.futureprocessing.documentjuggler.update.operators;

import com.futureprocessing.documentjuggler.update.UpdateBuilder;

public class UpdateOperators<TYPE> {

    private final String field;
    private final UpdateBuilder updateBuilder;

    public UpdateOperators(String field, UpdateBuilder updateBuilder) {
        this.field = field;
        this.updateBuilder = updateBuilder;
    }

    public UpdateOperators<TYPE> increment(TYPE value) {
        updateBuilder.inc(field, value);
        return this;
    }

    public UpdateOperators<TYPE> unset() {
        updateBuilder.unset(field);
        return this;
    }

    public UpdateOperators<TYPE> set(TYPE value) {
        updateBuilder.set(field, value);
        return this;
    }
}
