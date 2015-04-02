package com.futureprocessing.documentjuggler.insert;

import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.insert.command.InsertCommand;
import com.mongodb.BasicDBObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static java.lang.reflect.Proxy.getInvocationHandler;
import static java.lang.reflect.Proxy.newProxyInstance;

public class InsertProxy implements InvocationHandler {

    private final BasicDBObject document;
    private final Mapper<InsertCommand> mapper;

    @SuppressWarnings("unchecked")
    public static <INSERTER> INSERTER create(Class<INSERTER> inserterClass, Mapper<InsertCommand> mapper) {
        return (INSERTER) newProxyInstance(inserterClass.getClassLoader(), new Class[]{inserterClass},
                new InsertProxy(mapper));
    }

    public static InsertProxy extract(Object updater) {
        return (InsertProxy) getInvocationHandler(updater);
    }


    private InsertProxy(Mapper<InsertCommand> mapper) {
        this.document = new BasicDBObject();
        this.mapper = mapper;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        mapper.get(method).insert(document, args);
        return proxy;
    }

    public BasicDBObject getDocument() {
        return document.isEmpty() ? null : document;
    }

}
