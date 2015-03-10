package com.futureprocessing.mongojuggler.query.command;

import com.futureprocessing.mongojuggler.exception.validation.UnsupportedMethodException;
import com.mongodb.QueryBuilder;

import java.lang.reflect.Method;

public class UnsupportedQueryCommand implements QueryCommand {

    private final Method method;

    public UnsupportedQueryCommand(Method method) {
        this.method = method;
    }

    @Override
    public void query(QueryBuilder builder, Object[] args) {
        throw new UnsupportedMethodException(method);
    }
}
