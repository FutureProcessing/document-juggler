package com.futureprocessing.mongojuggler.write.command;


import com.futureprocessing.mongojuggler.write.UpdateBuilder;

public class PushUpdateCommand extends AbstractUpdateCommand {

    public PushUpdateCommand(String field) {
        super(field);
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        updateBuilder.push(field, args[0]);
    }
}
