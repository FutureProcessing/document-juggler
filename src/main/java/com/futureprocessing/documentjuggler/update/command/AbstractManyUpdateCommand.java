package com.futureprocessing.documentjuggler.update.command;


import com.futureprocessing.documentjuggler.update.UpdateBuilder;

public abstract class AbstractManyUpdateCommand extends AbstractUpdateCommand {

    public AbstractManyUpdateCommand(String field) {
        super(field);
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        for (Object o : args) {
            update(updateBuilder, o);
        }
    }

    public abstract void update(UpdateBuilder updateBuilder, Object value);
}
