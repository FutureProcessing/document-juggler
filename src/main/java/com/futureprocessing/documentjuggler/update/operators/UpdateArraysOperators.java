package com.futureprocessing.documentjuggler.update.operators;

import com.futureprocessing.documentjuggler.update.UpdateBuilder;

public class UpdateArraysOperators<TYPE> {
    private final String field;
    private final UpdateBuilder updateBuilder;

    public UpdateArraysOperators(String field, UpdateBuilder updateBuilder) {
        this.field = field;
        this.updateBuilder = updateBuilder;
    }


}
