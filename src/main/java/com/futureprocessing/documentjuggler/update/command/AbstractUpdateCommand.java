package com.futureprocessing.documentjuggler.update.command;


public abstract class AbstractUpdateCommand implements UpdateCommand {

    protected final String field;

    public AbstractUpdateCommand(String field) {
        this.field = field;
    }

}
