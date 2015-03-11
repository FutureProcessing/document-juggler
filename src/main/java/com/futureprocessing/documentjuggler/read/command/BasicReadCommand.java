package com.futureprocessing.documentjuggler.read.command;


import com.mongodb.BasicDBObject;

public class BasicReadCommand extends AbstractReadCommand {

    public BasicReadCommand(String field) {
        super(field);
    }

    @Override
    protected Object readValue(BasicDBObject document) {
        return document.get(field);
    }


}
