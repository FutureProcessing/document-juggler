package com.futureprocessing.mongojuggler.read.command;


import com.mongodb.BasicDBObject;

public class BooleanReadCommand extends AbstractReadCommand {

    public BooleanReadCommand(String field) {
        super(field);
    }

    @Override
    protected Object readValue(BasicDBObject document) {
        return document.getBoolean(field);
    }

}
