package com.futureprocessing.documentjuggler.insert.command;

import com.futureprocessing.documentjuggler.exception.validation.UnsupportedMethodException;
import com.mongodb.BasicDBObject;

import java.lang.reflect.Method;


public class UnsupportedInsertCommand implements InsertCommand {

    private final Method method;

    public UnsupportedInsertCommand(Method method) {
        this.method = method;
    }

    @Override
    public void insert(BasicDBObject document, Object[] args) {
        throw new UnsupportedMethodException(method);
    }
}
