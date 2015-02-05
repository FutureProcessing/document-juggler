package com.futureprocessing.mongojuggler.write;

import com.futureprocessing.mongojuggler.write.command.InsertCommand;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class InsertProxy implements InvocationHandler {

    private final BasicDBObject document;
    private final Map<Method, InsertCommand> insertCommands;

    public InsertProxy(Class<?> clazz) {
        this.document = new BasicDBObject();
        insertCommands = InsertMapper.INSTANCE.get(clazz);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        insertCommands.get(method).insert(document, args);
        return proxy;
    }

    public BasicDBObject getDocument() {
        return document.isEmpty() ? null : document;
    }

}
