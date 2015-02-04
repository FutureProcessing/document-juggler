package com.futureprocessing.mongojuggler.read.command;


import com.mongodb.BasicDBObject;

public class IdReadCommand extends AbstractReadCommand {


    public IdReadCommand() {
        super("_id");
    }

    @Override
    protected Object readValue(BasicDBObject document) {
        return document.getObjectId(field).toHexString();
    }
}
