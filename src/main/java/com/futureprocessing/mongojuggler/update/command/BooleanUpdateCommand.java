package com.futureprocessing.mongojuggler.update.command;


import com.futureprocessing.mongojuggler.update.UpdateBuilder;

public class BooleanUpdateCommand extends AbstractUpdateCommand {

    public BooleanUpdateCommand(String field) {
        super(field);
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        Boolean value = (Boolean) args[0];

        if (value != null && value) {
            updateBuilder.set(field, value);
        } else {
            updateBuilder.unset(field);
        }
    }
}
