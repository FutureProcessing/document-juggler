package com.futureprocessing.documentjuggler.update.command;

import com.futureprocessing.documentjuggler.update.UpdateBuilder;

public class UnsetCommand extends AbstractUpdateCommand {

    public UnsetCommand(String field) {
        super(field);
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        updateBuilder.unset(field);
    }
}
