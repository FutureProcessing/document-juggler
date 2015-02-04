package com.futureprocessing.mongojuggler.read.command;


import com.mongodb.BasicDBObject;

import java.util.Set;

public class IdReadCommand implements ReadCommand {

    @Override
    public Object read(BasicDBObject document, Set<String> queriedFields) {
        return document.getObjectId("_id").toHexString();
    }
}
