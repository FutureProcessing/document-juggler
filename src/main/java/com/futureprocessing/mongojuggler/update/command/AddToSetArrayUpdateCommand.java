package com.futureprocessing.mongojuggler.update.command;


import com.futureprocessing.mongojuggler.update.UpdateBuilder;

public class AddToSetArrayUpdateCommand extends AbstractUpdateCommand {

    public AddToSetArrayUpdateCommand(String field) {
        super(field);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        Object[] objects = (Object[]) args[0];
        for (Object o : objects) {
            updateBuilder.addToSet(field, o);
        }
    }
}
