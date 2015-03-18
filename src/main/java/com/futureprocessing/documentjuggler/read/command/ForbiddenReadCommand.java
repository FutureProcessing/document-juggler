package com.futureprocessing.documentjuggler.read.command;


import com.futureprocessing.documentjuggler.exception.ForbiddenOperationException;
import com.mongodb.BasicDBObject;

import java.lang.reflect.Method;
import java.util.Set;

import static com.futureprocessing.documentjuggler.Context.READ;

public class ForbiddenReadCommand implements ReadCommand {

    private final Method method;

    public ForbiddenReadCommand(Method method) {
        this.method = method;
    }


    @Override
    public Object read(BasicDBObject document, Set<String> queriedFields) {
        throw new ForbiddenOperationException(method, READ);
    }
}
