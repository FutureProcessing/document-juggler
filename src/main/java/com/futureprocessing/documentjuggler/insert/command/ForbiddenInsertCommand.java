package com.futureprocessing.documentjuggler.insert.command;


import com.futureprocessing.documentjuggler.exception.ForbiddenActionException;
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
        throw new ForbiddenActionException(method, INSERT);
    }
}
