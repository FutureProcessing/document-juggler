package com.futureprocessing.mongojuggler.update.command;


import com.futureprocessing.mongojuggler.update.UpdateBuilder;

public class BooleanUpdateCommand extends AbstractUpdateCommand {

    private final boolean unsetIfNull;
    private final boolean unsetIfFalse;

    public BooleanUpdateCommand(String field, boolean unsetIfNull, boolean unsetIfFalse) {
        super(field);
        this.unsetIfNull = unsetIfNull;
        this.unsetIfFalse = unsetIfFalse;
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        Boolean value = (Boolean) args[0];

        if (unsetIfNull && value == null) {
            updateBuilder.unset(field);
            return;
        }

        if (unsetIfFalse && value != null && !value) {
            updateBuilder.unset(field);
            return;
        }

        updateBuilder.set(field, value);
    }
}
