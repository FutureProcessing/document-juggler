package com.futureprocessing.mongojuggler.write;

import com.futureprocessing.mongojuggler.write.command.InsertCommand;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import static java.lang.reflect.Proxy.getInvocationHandler;
import static java.lang.reflect.Proxy.newProxyInstance;

public class InsertProxy implements InvocationHandler {

    private final BasicDBObject document;
    private final Map<Method, InsertCommand> insertCommands;

    @SuppressWarnings("unchecked")
    public static <UPDATER> UPDATER create(Class<UPDATER> updaterClass, InsertMapper mapper) {
        return (UPDATER) newProxyInstance(updaterClass.getClassLoader(), new Class[]{updaterClass}, new InsertProxy(updaterClass, mapper));
    }

    public static InsertProxy extract(Object updater) {
        return (InsertProxy) getInvocationHandler(updater);
    }


    private InsertProxy(Class<?> clazz, InsertMapper mapper) {
        this.document = new BasicDBObject();
        insertCommands = mapper.get(clazz);
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
