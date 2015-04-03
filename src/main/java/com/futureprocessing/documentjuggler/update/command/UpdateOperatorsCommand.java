package com.futureprocessing.documentjuggler.update.command;

import com.futureprocessing.documentjuggler.query.operators.Comparison;
import com.futureprocessing.documentjuggler.query.operators.ComparisonOperators;
import com.futureprocessing.documentjuggler.update.UpdateBuilder;
import com.futureprocessing.documentjuggler.update.operators.Update;
import com.futureprocessing.documentjuggler.update.operators.UpdateOperators;

public class UpdateOperatorsCommand extends AbstractUpdateCommand {

    public UpdateOperatorsCommand(String field) {
        super(field);
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        Update consumer = (Update) args[0];
        UpdateOperators operators = new UpdateOperators(field, updateBuilder);
        consumer.apply(operators);
    }
}
