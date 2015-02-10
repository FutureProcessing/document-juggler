package com.futureprocessing.mongojuggler.write.command;


import com.futureprocessing.mongojuggler.write.UpdateBuilder;

public class AddToSetUpdateCommand extends AbstractUpdateCommand {

    public AddToSetUpdateCommand(String field) {
        super(field);
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        updateBuilder.addToSet(field, args[0]);
    }
}
