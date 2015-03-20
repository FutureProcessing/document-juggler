package com.futureprocessing.documentjuggler.query.command;

import com.mongodb.QueryBuilder;

public class GreaterThanQueryCommand implements QueryCommand {

    private final String field;

    public GreaterThanQueryCommand(String field) {
        this.field = field;
    }

    @Override
    public void query(QueryBuilder builder, Object[] args) {
        builder.and(field).greaterThan(args[0]);
    }
}
