package com.futureprocessing.mongojuggler.read.command;


import com.mongodb.BasicDBObject;

public interface ReadCommand {

    Object read(BasicDBObject document);
}
