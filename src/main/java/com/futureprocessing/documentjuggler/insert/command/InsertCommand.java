package com.futureprocessing.documentjuggler.insert.command;


import com.mongodb.BasicDBObject;

public interface InsertCommand {

    void insert(BasicDBObject document, Object[] args);
}
