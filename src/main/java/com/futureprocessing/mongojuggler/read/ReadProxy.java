package com.futureprocessing.mongojuggler.read;

import com.futureprocessing.mongojuggler.read.command.ReadCommand;
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
    private final Map<Method, ReadCommand> readCommands;
    private final Map<Method, Object> cache = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <READER> READER create(Class<READER> readerType, Map<Method, ReadCommand> readCommands,
                                         DBObject dbObject, Set<String> fields) {
        return (READER) newProxyInstance(readerType.getClassLoader(), new Class[]{readerType},
                                         new ReadProxy(readerType, readCommands, (BasicDBObject) dbObject, fields));
    }

    private ReadProxy(Class readerType, Map<Method, ReadCommand> readCommands, BasicDBObject dbObject,
                      Set<String> queriedFields) {
        this.dbObject = dbObject;
        this.queriedFields = queriedFields;
        this.readCommands = readCommands;

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

        Object value = readCommands.get(method).read(dbObject, queriedFields);
        cache.put(method, value);
        return value;
    }

}
