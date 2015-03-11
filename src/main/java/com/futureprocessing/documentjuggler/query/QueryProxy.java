package com.futureprocessing.documentjuggler.query;

import com.futureprocessing.documentjuggler.query.command.QueryCommand;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import static java.lang.reflect.Proxy.getInvocationHandler;
import static java.lang.reflect.Proxy.newProxyInstance;

public class QueryProxy implements InvocationHandler {

    private final QueryBuilder builder = new QueryBuilder();
    private final Map<Method, QueryCommand> queryCommands;

    @SuppressWarnings("unchecked")
    public static <QUERY> QUERY create(Class<QUERY> queryClass, Map<Method, QueryCommand> queryCommands) {
        return (QUERY) newProxyInstance(queryClass.getClassLoader(), new Class[]{queryClass},
                                        new QueryProxy(queryCommands));
    }

    public static QueryProxy extract(Object query) {
        return (QueryProxy) getInvocationHandler(query);
    }

    private QueryProxy(Map<Method, QueryCommand> queryCommands) {
        this.queryCommands = queryCommands;
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
