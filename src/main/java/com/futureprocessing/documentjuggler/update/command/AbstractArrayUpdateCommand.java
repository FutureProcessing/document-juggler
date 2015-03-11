package com.futureprocessing.documentjuggler.update.command;


import com.futureprocessing.documentjuggler.update.UpdateBuilder;

public abstract class AbstractArrayUpdateCommand extends AbstractUpdateCommand {

    public AbstractArrayUpdateCommand(String field) {
        super(field);
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        Object[] objects = (Object[]) args[0];
        for (Object o : objects) {
            update(updateBuilder, o);
        }
    }

    protected abstract void update(UpdateBuilder updateBuilder, Object value);
}
