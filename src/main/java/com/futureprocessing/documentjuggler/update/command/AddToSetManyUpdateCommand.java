package com.futureprocessing.documentjuggler.update.command;


import com.futureprocessing.documentjuggler.update.UpdateBuilder;

public class AddToSetManyUpdateCommand extends AbstractManyUpdateCommand {

    public AddToSetManyUpdateCommand(String field) {
        super(field);
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object value) {
        updateBuilder.addToSet(field, value);
    }
}
