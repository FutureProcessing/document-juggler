package com.futureprocessing.mongojuggler.read.command;

import com.mongodb.QueryBuilder;

public class BasicQueryCommand implements QueryCommand{

    private final String field;

    public BasicQueryCommand(String field) {
        this.field = field;
    }

    @Override
    public void query(QueryBuilder builder, Object[] args) {
        builder.and(field).is(args[0]);
    }
}
