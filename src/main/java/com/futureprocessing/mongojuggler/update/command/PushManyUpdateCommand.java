package com.futureprocessing.mongojuggler.update.command;


import com.futureprocessing.mongojuggler.update.UpdateBuilder;

public class PushManyUpdateCommand extends AbstractManyUpdateCommand {

    public PushManyUpdateCommand(String field) {
        super(field);
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object value) {
        updateBuilder.push(field, value);
    }
}
