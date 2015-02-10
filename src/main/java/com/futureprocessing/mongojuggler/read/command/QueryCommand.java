package com.futureprocessing.mongojuggler.read.command;

import com.mongodb.QueryBuilder;

public interface QueryCommand {

    void query(QueryBuilder builder, Object[] args);
}
