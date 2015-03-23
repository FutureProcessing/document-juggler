package com.futureprocessing.documentjuggler.query.command;

import com.mongodb.QueryBuilder;

public class GreaterThanEqualQueryCommand implements QueryCommand {

    private final String field;

    public GreaterThanEqualQueryCommand(String field) {
        this.field = field;
    }

    @Override
    public void query(QueryBuilder builder, Object[] args) {
        builder.and(field).greaterThanEquals(args[0]);
    }
}
