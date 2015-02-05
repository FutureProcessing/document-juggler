package com.futureprocessing.mongojuggler.write.command;

import com.futureprocessing.mongojuggler.exception.UnsupportedActionException;
import com.mongodb.BasicDBObject;


public class UnsupportedInsertCommand implements InsertCommand {

    @Override
    public void insert(BasicDBObject document, Object[] args) {
        throw new UnsupportedActionException();
    }
}
