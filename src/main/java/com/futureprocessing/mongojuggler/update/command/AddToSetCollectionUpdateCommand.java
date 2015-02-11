package com.futureprocessing.mongojuggler.update.command;


import com.futureprocessing.mongojuggler.update.UpdateBuilder;

public class AddToSetCollectionUpdateCommand extends AbstractCollectionUpdateCommand {

    public AddToSetCollectionUpdateCommand(String field) {
        super(field);
    }

    @Override
    protected void update(UpdateBuilder updateBuilder, Object value) {
        updateBuilder.addToSet(field, value);
    }
}
