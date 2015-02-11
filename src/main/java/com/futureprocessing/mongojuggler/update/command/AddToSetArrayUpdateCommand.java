package com.futureprocessing.mongojuggler.update.command;


import com.futureprocessing.mongojuggler.update.UpdateBuilder;

public class AddToSetArrayUpdateCommand extends AbstractArrayUpdateCommand {

    public AddToSetArrayUpdateCommand(String field) {
        super(field);
    }

    @Override
    protected void update(UpdateBuilder updateBuilder, Object value) {
        updateBuilder.addToSet(field, value);
    }
}
