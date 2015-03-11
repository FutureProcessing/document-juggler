package com.futureprocessing.documentjuggler.read.command;

import com.futureprocessing.documentjuggler.exception.validation.UnsupportedMethodException;
import com.mongodb.BasicDBObject;

import java.lang.reflect.Method;
import java.util.Set;

public class UnsupportedReadCommand implements ReadCommand {

    private final Method method;

    public UnsupportedReadCommand(Method method) {
        this.method = method;
    }

    @Override
    public Object read(BasicDBObject document, Set<String> queriedFields) {
        throw new UnsupportedMethodException(method);
    }
}
