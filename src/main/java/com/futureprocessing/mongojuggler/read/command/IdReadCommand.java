package com.futureprocessing.mongojuggler.read.command;


import com.mongodb.BasicDBObject;

public class IdReadCommand implements ReadCommand {

    @Override
    public Object read(BasicDBObject document) {
        return document.getObjectId("_id").toHexString();
    }
}
