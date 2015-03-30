package com.futureprocessing.documentjuggler.query.command;

import com.mongodb.QueryBuilder;

public class NotQueryCommand implements QueryCommand {

    private final String field;
    private final QueryCommand operandCommand;

    public NotQueryCommand(String field, QueryCommand operandCommand) {
        this.field = field;
        this.operandCommand = operandCommand;
    }

    @Override
    public void query(QueryBuilder builder, Object[] args) {

        operandCommand.query(builder, args);

        Object operand = builder.get().get(field);
        builder.and(field).not().is(operand);
    }
}
