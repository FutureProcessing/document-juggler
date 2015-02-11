package com.futureprocessing.mongojuggler.update.command;


import com.futureprocessing.mongojuggler.update.UpdateBuilder;

public class AddToSetSingleUpdateCommand extends AbstractUpdateCommand {

    public AddToSetSingleUpdateCommand(String field) {
        super(field);
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        updateBuilder.addToSet(field, args[0]);
    }
}
