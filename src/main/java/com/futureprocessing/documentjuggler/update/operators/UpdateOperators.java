package com.futureprocessing.documentjuggler.update.operators;

import com.futureprocessing.documentjuggler.update.UpdateBuilder;
import com.futureprocessing.documentjuggler.update.command.IncrementUpdateCommand;
import com.futureprocessing.documentjuggler.update.command.UnsetCommand;

public class UpdateOperators<TYPE> {

    private final String field;
    private final UpdateBuilder updateBuilder;

    public UpdateOperators(String field, UpdateBuilder updateBuilder) {
        this.field = field;
        this.updateBuilder = updateBuilder;
    }

    public UpdateOperators<TYPE> increment(TYPE value) {
        IncrementUpdateCommand.update(field, updateBuilder, value);
        return this;
    }

    public UpdateOperators<TYPE> unset() {
        UnsetCommand.update(field, updateBuilder);
        return this;
    }

    public UpdateOperators<TYPE> set(TYPE value) {
        updateBuilder.set(field, value);
        return this;
    }
}
