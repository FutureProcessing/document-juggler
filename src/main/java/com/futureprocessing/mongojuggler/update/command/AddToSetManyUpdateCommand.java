package com.futureprocessing.mongojuggler.update.command;


import com.futureprocessing.mongojuggler.update.UpdateBuilder;

public class AddToSetManyUpdateCommand extends AbstractUpdateCommand {

    public AddToSetManyUpdateCommand(String field) {
        super(field);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        for (Object o : args) {
            updateBuilder.addToSet(field, o);
        }
    }
}
