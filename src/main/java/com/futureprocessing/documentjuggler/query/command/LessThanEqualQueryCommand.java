package com.futureprocessing.documentjuggler.query.command;

import com.mongodb.QueryBuilder;

public class LessThanEqualQueryCommand implements QueryCommand {

    private final String field;

    public LessThanEqualQueryCommand(String field) {
        this.field = field;
    }

    @Override
    public void query(QueryBuilder builder, Object[] args) {
        builder.and(field).lessThanEquals(args[0]);
    }
}
