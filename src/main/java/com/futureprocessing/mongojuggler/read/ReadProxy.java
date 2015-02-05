package com.futureprocessing.mongojuggler.read;

import com.futureprocessing.mongojuggler.read.command.ReadCommand;
import com.mongodb.BasicDBObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;


public class ReadProxy implements InvocationHandler {

    private final BasicDBObject dbObject;
    private final Set<String> queriedFields;
    private final Map<Method, ReadCommand> readCommands;

    public ReadProxy(Class<?> clazz, BasicDBObject dbObject, Set<String> queriedFields) {
        if (dbObject == null) {
            throw new RuntimeException("Null dbObject");
        }
        this.dbObject = dbObject;
        this.queriedFields = queriedFields;
        readCommands = ReadMapper.INSTANCE.get(clazz);
    }

    @Override
    public Object invoke(Object object, Method method, Object[] params) throws Throwable {
        return readCommands.get(method).read(dbObject, queriedFields);
    }

}
