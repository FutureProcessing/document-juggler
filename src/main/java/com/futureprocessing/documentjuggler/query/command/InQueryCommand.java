package com.futureprocessing.documentjuggler.query.command;

import com.mongodb.QueryBuilder;

public class InQueryCommand implements QueryCommand {

    private final String field;

    public InQueryCommand(String field) {
        this.field = field;
    }

    @Override
    public void query(QueryBuilder builder, Object[] args) {
        builder.and(field).in(args[0]);
    }
}
