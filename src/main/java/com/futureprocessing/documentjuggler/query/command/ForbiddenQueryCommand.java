package com.futureprocessing.documentjuggler.query.command;


import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.exception.ForbiddenOperationException;
import com.mongodb.QueryBuilder;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.Context.QUERY;

public class ForbiddenQueryCommand implements QueryCommand {

    private final Method method;

    public ForbiddenQueryCommand(Method method) {
        this.method = method;
    }

    @Override
    public void query(QueryBuilder builder, Object[] args) {
        throw new ForbiddenOperationException(method, QUERY);
    }

    public static class Provider implements CommandProvider<QueryCommand> {
        @Override
        public QueryCommand getCommand(Method method, Mapper<QueryCommand> mapper) {
            return new ForbiddenQueryCommand(method);
        }
    }
}