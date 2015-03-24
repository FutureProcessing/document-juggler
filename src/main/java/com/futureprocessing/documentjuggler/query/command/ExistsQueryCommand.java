package com.futureprocessing.documentjuggler.query.command;

import com.mongodb.QueryBuilder;

public class ExistsQueryCommand implements QueryCommand {

    private final String field;

    public ExistsQueryCommand(String field) {
        this.field = field;
    }

    @Override
    public void query(QueryBuilder builder, Object[] args) {
        builder.and(field).exists(args[0]);
    }
}
