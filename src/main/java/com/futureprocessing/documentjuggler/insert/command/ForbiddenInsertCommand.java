package com.futureprocessing.documentjuggler.insert.command;


import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.exception.ForbiddenOperationException;
import com.mongodb.BasicDBObject;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.Context.INSERT;

public class ForbiddenInsertCommand implements InsertCommand {

    private final Method method;

    public ForbiddenInsertCommand(Method method) {
        this.method = method;
    }

    @Override
    public void insert(BasicDBObject document, Object[] args) {
        throw new ForbiddenOperationException(method, INSERT);
    }

    public static class Provider implements CommandProvider<InsertCommand> {
        @Override
        public InsertCommand getCommand(Method method, Mapper<InsertCommand> mapper) {
            return new ForbiddenInsertCommand(method);
        }
    }
}
