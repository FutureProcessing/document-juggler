package com.futureprocessing.mongojuggler.read.command;


import com.mongodb.BasicDBObject;

public class BasicReadCommand implements ReadCommand {

    private final String field;

    public BasicReadCommand(String field) {
        this.field = field;
    }

    @Override
    public Object read(BasicDBObject document) {
        return document.get(field);
    }
}
