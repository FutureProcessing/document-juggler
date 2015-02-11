package com.futureprocessing.mongojuggler.update.command;


import com.futureprocessing.mongojuggler.update.UpdateBuilder;

public class PushCollectionUpdateCommand extends AbstractCollectionUpdateCommand {

    public PushCollectionUpdateCommand(String field) {
        super(field);
    }

    @Override
    protected void update(UpdateBuilder updateBuilder, Object value) {
        updateBuilder.push(field, value);
    }
}
