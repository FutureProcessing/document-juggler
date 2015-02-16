package com.futureprocessing.mongojuggler.update.command;


import com.futureprocessing.mongojuggler.update.UpdateBuilder;

public class IncrementUpdateCommand extends AbstractUpdateCommand{

    public IncrementUpdateCommand(String field) {
        super(field);
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        updateBuilder.inc(field, (Integer) args[0]);
    }
}
