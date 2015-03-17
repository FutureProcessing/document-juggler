package com.futureprocessing.documentjuggler.read.command;


import com.mongodb.BasicDBObject;

import java.util.Set;

public class IdReadCommand extends AbstractReadCommand {


    public IdReadCommand(String field) {
        super(field);
    }

    @Override
    protected Object readValue(BasicDBObject document) {
        return document.getObjectId(field).toHexString();
    }
}
