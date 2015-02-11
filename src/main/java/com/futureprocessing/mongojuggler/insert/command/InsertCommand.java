package com.futureprocessing.mongojuggler.insert.command;


import com.mongodb.BasicDBObject;

public interface InsertCommand {

    void insert(BasicDBObject document, Object[] args);
}
