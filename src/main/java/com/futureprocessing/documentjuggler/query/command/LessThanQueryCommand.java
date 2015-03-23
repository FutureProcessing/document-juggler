package com.futureprocessing.documentjuggler.query.command;

import com.mongodb.QueryBuilder;

public class LessThanQueryCommand implements QueryCommand {

    private final String field;

    public LessThanQueryCommand(String field) {
        this.field = field;
    }

    @Override
    public void query(QueryBuilder builder, Object[] args) {
        builder.and(field).lessThan(args[0]);
    }
}
