package com.futureprocessing.mongojuggler.write.command;


import com.mongodb.BasicDBObject;

public interface InsertCommand {

    void insert(BasicDBObject document, Object[] args);
}
