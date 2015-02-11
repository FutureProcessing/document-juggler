package com.futureprocessing.mongojuggler.update.command;


import com.futureprocessing.mongojuggler.update.UpdateBuilder;

public class PushArrayUpdateCommand extends AbstractArrayUpdateCommand {

    public PushArrayUpdateCommand(String field) {
        super(field);
    }

    @Override
    protected void update(UpdateBuilder updateBuilder, Object value) {
        updateBuilder.push(field, value);
    }
}
