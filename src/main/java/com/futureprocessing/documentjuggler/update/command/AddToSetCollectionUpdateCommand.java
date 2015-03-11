package com.futureprocessing.documentjuggler.update.command;


import com.futureprocessing.documentjuggler.update.UpdateBuilder;

public class AddToSetCollectionUpdateCommand extends AbstractCollectionUpdateCommand {

    public AddToSetCollectionUpdateCommand(String field) {
        super(field);
    }

    @Override
    protected void update(UpdateBuilder updateBuilder, Object value) {
        updateBuilder.addToSet(field, value);
    }
}
