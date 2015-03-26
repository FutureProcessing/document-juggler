package com.futureprocessing.documentjuggler.read;

import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.read.command.ReadCommand;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.reflect.Proxy.newProxyInstance;


public class ReadProxy implements InvocationHandler {

    private final BasicDBObject dbObject;
    private final Set<String> queriedFields;
    private final Mapper<ReadCommand> mapper;
    private final Map<Method, Object> cache = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <READER> READER create(Class<READER> readerType, Mapper<ReadCommand> mapper,
                                         DBObject dbObject, Set<String> fields) {
        return (READER) newProxyInstance(readerType.getClassLoader(), new Class[]{readerType},
                new ReadProxy(readerType, mapper, (BasicDBObject) dbObject, fields));
    }

    private ReadProxy(Class readerType, Mapper<ReadCommand> mapper, BasicDBObject dbObject,
                      Set<String> queriedFields) {
        this.dbObject = dbObject;
        this.queriedFields = queriedFields;
        this.mapper = mapper;

        if (dbObject == null) {
            throw new RuntimeException("Null dbObject");
        }
    }

    @Override
    public Object invoke(Object object, Method method, Object[] params) throws Throwable {
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

}
