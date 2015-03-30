package com.futureprocessing.documentjuggler.read;


import com.futureprocessing.documentjuggler.commons.EqualsProvider;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.read.command.ReadCommand;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.futureprocessing.documentjuggler.commons.EqualsProvider.Creator.fromAnnotation;
import static com.futureprocessing.documentjuggler.commons.Methods.EQUALS_METHOD;
import static java.lang.reflect.Proxy.getInvocationHandler;
import static java.lang.reflect.Proxy.newProxyInstance;


public class ReadProxy implements InvocationHandler {

    private final BasicDBObject dbObject;
    private final Set<String> queriedFields;
    private final Mapper<ReadCommand> mapper;
    private final Map<Method, Object> cache = new HashMap<>();
    private final EqualsProvider equalsProvider;

    @SuppressWarnings("unchecked")
    public static <READER> READER create(Class<READER> readerType, Mapper<ReadCommand> mapper,
                                         DBObject dbObject, Set<String> fields) {
        return (READER) newProxyInstance(readerType.getClassLoader(), new Class[]{readerType},
                new ReadProxy(readerType, mapper, (BasicDBObject) dbObject, fields));

    }

    private ReadProxy(Class readerType, Mapper<ReadCommand> mapper, BasicDBObject dbObject,
                      Set<String> queriedFields) {
        if (dbObject == null) {
            throw new RuntimeException("Null dbObject");
        }

        this.dbObject = dbObject;
        this.queriedFields = queriedFields;
        this.mapper = mapper;

        this.equalsProvider = fromAnnotation(readerType);
    }

    @Override
    public Object invoke(Object object, Method method, Object[] params) throws Throwable {

        if (method.equals(EQUALS_METHOD)) {
            return areEqual(object, params[0]);
        }

        if (method.getDeclaringClass().equals(Object.class)) {
            return method.invoke(this, params);
        }

        if (cache.containsKey(method)) {
            return cache.get(method);
        }

        Object value = mapper.get(method).read(dbObject, queriedFields);
        cache.put(method, value);
        return value;
    }

    private Object areEqual(Object object, Object param) {

        InvocationHandler invocationHandler = getInvocationHandler(param);
        if (!(invocationHandler instanceof ReadProxy)) {
            return false;
        }

        if (equalsProvider != null) {
            return equalsProvider.areEqual(object, param);
        }

        ReadProxy otherProxy = (ReadProxy) invocationHandler;
        return dbObject.equals(otherProxy.dbObject);
    }

}
