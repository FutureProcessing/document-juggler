package com.futureprocessing.mongojuggler.read;

import com.futureprocessing.mongojuggler.read.command.QueryCommand;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class QueryProxy implements InvocationHandler {

    private final QueryBuilder builder = new QueryBuilder();
    private final Map<Method, QueryCommand> queryCommands;

    public QueryProxy(Class<?> clazz, QueryMapper mapper) {
        queryCommands = mapper.get(clazz);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        queryCommands.get(method).query(builder, args);
        return proxy;
    }

    public DBObject toDBObject() {
        return builder.get();
    }
}
