package com.futureprocessing.mongojuggler.update.command;


import com.futureprocessing.mongojuggler.update.UpdateBuilder;

public class BasicUpdateCommand extends AbstractUpdateCommand {

    private final boolean unsetIfNull;

    public BasicUpdateCommand(String field, boolean unsetIfNull) {
        super(field);
        this.unsetIfNull = unsetIfNull;
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        Object value = args[0];

        if (unsetIfNull && value == null) {
            updateBuilder.unset(field);
            return;
        }

        updateBuilder.set(field, value);
    }
}
