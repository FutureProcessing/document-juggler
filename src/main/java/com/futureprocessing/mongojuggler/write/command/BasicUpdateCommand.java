package com.futureprocessing.mongojuggler.write.command;


import com.futureprocessing.mongojuggler.write.UpdateBuilder;

public class BasicUpdateCommand extends AbstractUpdateCommand{

    public BasicUpdateCommand(String field) {
        super(field);
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        Object value = args[0];

        if (value != null) {
            updateBuilder.set(field, value);
        } else {
            updateBuilder.unset(field);
        }
    }
}
