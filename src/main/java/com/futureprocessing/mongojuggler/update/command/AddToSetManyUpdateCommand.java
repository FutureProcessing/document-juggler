package com.futureprocessing.mongojuggler.update.command;


import com.futureprocessing.mongojuggler.update.UpdateBuilder;

public class AddToSetManyUpdateCommand extends AbstractManyUpdateCommand {

    public AddToSetManyUpdateCommand(String field) {
        super(field);
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object value) {
        updateBuilder.addToSet(field, value);
    }
}
