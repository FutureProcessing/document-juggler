package com.futureprocessing.documentjuggler.update.command;


import com.futureprocessing.documentjuggler.update.UpdateBuilder;

public class PushSingleUpdateCommand extends AbstractUpdateCommand {

    public PushSingleUpdateCommand(String field) {
        super(field);
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        updateBuilder.push(field, args[0]);
    }
}
