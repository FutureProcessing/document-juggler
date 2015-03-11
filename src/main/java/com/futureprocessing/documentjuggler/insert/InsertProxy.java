package com.futureprocessing.documentjuggler.insert;

import com.futureprocessing.documentjuggler.insert.command.InsertCommand;
import com.mongodb.BasicDBObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import static java.lang.reflect.Proxy.getInvocationHandler;
import static java.lang.reflect.Proxy.newProxyInstance;

public class InsertProxy implements InvocationHandler {

    private final BasicDBObject document;
    private final Map<Method, InsertCommand> insertCommands;

    @SuppressWarnings("unchecked")
    public static <INSERTER> INSERTER create(Class<INSERTER> inserterClass, Map<Method, InsertCommand> insertCommands) {
        return (INSERTER) newProxyInstance(inserterClass.getClassLoader(), new Class[]{inserterClass},
                                           new InsertProxy(insertCommands));
    }

    public static InsertProxy extract(Object updater) {
        return (InsertProxy) getInvocationHandler(updater);
    }


    private InsertProxy(Map<Method, InsertCommand> insertCommands) {
        this.document = new BasicDBObject();
        this.insertCommands = insertCommands;
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
