package com.futureprocessing.mongojuggler.query.command;

import com.mongodb.QueryBuilder;

public interface QueryCommand {

    void query(QueryBuilder builder, Object[] args);
}
