package com.futureprocessing.mongojuggler.insert.command;


public abstract class AbstractInsertCommand implements InsertCommand {

    protected final String field;

    public AbstractInsertCommand(String field) {
        this.field = field;
    }
}
