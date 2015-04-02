package com.futureprocessing.documentjuggler.update.command;

import com.futureprocessing.documentjuggler.update.UpdateBuilder;
import com.futureprocessing.documentjuggler.update.operators.UpdateArrays;
import com.futureprocessing.documentjuggler.update.operators.UpdateArraysOperators;

public class UpdateArraysOperatorsCommand extends AbstractUpdateCommand {

    public UpdateArraysOperatorsCommand(String field) {
        super(field);
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        UpdateArrays consumer = (UpdateArrays) args[0];
        UpdateArraysOperators operators = new UpdateArraysOperators(field, updateBuilder);
        consumer.apply(operators);
    }
}
