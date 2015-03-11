package com.futureprocessing.documentjuggler.query.command;

import com.futureprocessing.documentjuggler.exception.validation.UnsupportedMethodException;
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
