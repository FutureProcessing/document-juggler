package com.futureprocessing.mongojuggler.update.command;


import com.futureprocessing.mongojuggler.update.UpdateBuilder;

public class BasicUpdateCommand extends AbstractUpdateCommand {

    public BasicUpdateCommand(String field) {
        super(field);
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        Object value = args[0];

        updateBuilder.set(field, value);
    }
}
