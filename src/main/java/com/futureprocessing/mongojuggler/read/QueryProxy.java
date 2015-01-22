package com.futureprocessing.mongojuggler.read;

import com.futureprocessing.mongojuggler.annotation.DbField;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class QueryProxy implements InvocationHandler {

    private final QueryBuilder builder = new QueryBuilder();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        DbField field = method.getAnnotation(DbField.class);
        builder.and(field.value()).is(args[0]);
        return proxy;
    }

    public DBObject toDBObject() {
        return builder.get();
    }
}
