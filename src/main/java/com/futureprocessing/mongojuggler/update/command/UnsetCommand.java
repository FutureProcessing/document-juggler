package com.futureprocessing.mongojuggler.update.command;

import com.futureprocessing.mongojuggler.update.UpdateBuilder;

public class UnsetCommand extends AbstractUpdateCommand {

    public UnsetCommand(String field) {
        super(field);
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        updateBuilder.unset(field);
    }
}
