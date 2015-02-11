package com.futureprocessing.mongojuggler.update.command;


import com.futureprocessing.mongojuggler.update.UpdateBuilder;

public class PushSingleUpdateCommand extends AbstractUpdateCommand {

    public PushSingleUpdateCommand(String field) {
        super(field);
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        updateBuilder.push(field, args[0]);
    }
}
