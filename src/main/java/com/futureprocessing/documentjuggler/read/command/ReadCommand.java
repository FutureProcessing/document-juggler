package com.futureprocessing.documentjuggler.read.command;


import com.mongodb.BasicDBObject;

import java.util.Set;

public interface ReadCommand {

    Object read(BasicDBObject document, Set<String> queriedFields);
}
