package com.futureprocessing.documentjuggler.update.operators;

import com.futureprocessing.documentjuggler.update.UpdateBuilder;
import com.futureprocessing.documentjuggler.update.command.AddToSetCollectionUpdateCommand;
import com.futureprocessing.documentjuggler.update.command.AddToSetManyUpdateCommand;
import com.futureprocessing.documentjuggler.update.command.PushCollectionUpdateCommand;
import com.futureprocessing.documentjuggler.update.command.PushManyUpdateCommand;

import java.util.Collection;

public class UpdateArraysOperators<TYPE> {
    private final String field;
    private final UpdateBuilder updateBuilder;

    public UpdateArraysOperators(String field, UpdateBuilder updateBuilder) {
        this.field = field;
        this.updateBuilder = updateBuilder;
    }


    public UpdateArraysOperators<TYPE> addToSet(TYPE... value) {
        new AddToSetManyUpdateCommand(field).update(updateBuilder, value);
        return this;
    }

    public UpdateArraysOperators<TYPE> addToSet(Collection<TYPE> collection) {
        new AddToSetCollectionUpdateCommand(field).update(updateBuilder, new Object[]{collection});
        return this;
    }

    public UpdateArraysOperators<TYPE> push(TYPE... value) {
        new PushManyUpdateCommand(field).update(updateBuilder, value);
        return this;
    }

    public UpdateArraysOperators<TYPE> push(Collection<TYPE> collection) {
        new PushCollectionUpdateCommand(field).update(updateBuilder, new Object[]{collection});
        return this;
    }
}
