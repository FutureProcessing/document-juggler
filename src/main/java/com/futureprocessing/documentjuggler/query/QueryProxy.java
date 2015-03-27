package com.futureprocessing.documentjuggler.query;

import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.query.command.QueryCommand;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static java.lang.reflect.Proxy.getInvocationHandler;
import static java.lang.reflect.Proxy.newProxyInstance;

public class QueryProxy implements InvocationHandler {

    private final QueryBuilder builder = new QueryBuilder();
    private final Mapper<QueryCommand> mapper;

    @SuppressWarnings("unchecked")
    public static <QUERY> QUERY create(Class<QUERY> queryClass, Mapper<QueryCommand> mapper) {
        return (QUERY) newProxyInstance(queryClass.getClassLoader(), new Class[]{queryClass}, new QueryProxy(mapper));
    }

    public static QueryProxy extract(Object query) {
        return (QueryProxy) getInvocationHandler(query);
    }

    private QueryProxy(Mapper<QueryCommand> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        mapper.get(method).query(builder, args);
        return proxy;
    }

    public DBObject toDBObject() {
        return builder.get();
    }
}
