package com.futureprocessing.mongojuggler.read;

import com.futureprocessing.mongojuggler.read.command.ReadCommand;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import static java.lang.reflect.Proxy.newProxyInstance;


public class ReadProxy implements InvocationHandler {

    private final BasicDBObject dbObject;
    private final Set<String> queriedFields;
    private final Map<Method, ReadCommand> readCommands;

    @SuppressWarnings("unchecked")
    public static <READER> READER create(Class<READER> readerType, ReadMapper mapper, DBObject dbObject, Set<String> fields) {
        return (READER) newProxyInstance(readerType.getClassLoader(), new Class[]{readerType},
                new ReadProxy(readerType, mapper, (BasicDBObject) dbObject, fields));
    }

    private ReadProxy(Class<?> clazz, ReadMapper mapper, BasicDBObject dbObject, Set<String> queriedFields) {
        this.dbObject = dbObject;
        this.queriedFields = queriedFields;

        if (dbObject == null) {
            throw new RuntimeException("Null dbObject");
        }

        readCommands = mapper.get(clazz);
    }

    @Override
    public Object invoke(Object object, Method method, Object[] params) throws Throwable {
        return readCommands.get(method).read(dbObject, queriedFields);
    }

}
