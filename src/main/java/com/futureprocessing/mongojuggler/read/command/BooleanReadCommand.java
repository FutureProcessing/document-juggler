package com.futureprocessing.mongojuggler.read.command;


import com.mongodb.BasicDBObject;

public class BooleanReadCommand implements ReadCommand {

    private final String field;

    public BooleanReadCommand(String field) {
        this.field = field;
    }

    @Override
    public Object read(BasicDBObject document) {
        return document.getBoolean(field);
    }
}
