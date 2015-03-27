package com.futureprocessing.documentjuggler.update;

import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.update.command.UpdateCommand;
import com.mongodb.BasicDBObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static java.lang.reflect.Proxy.getInvocationHandler;
import static java.lang.reflect.Proxy.newProxyInstance;

public class UpdateProxy implements InvocationHandler {

    private final UpdateBuilder updateBuilder;
    private final Mapper<UpdateCommand> mapper;

    @SuppressWarnings("unchecked")
    public static <UPDATER> UPDATER create(Class<UPDATER> updaterClass, Mapper<UpdateCommand> mapper,
                                           UpdateBuilder updateBuilder) {
        return (UPDATER) newProxyInstance(updaterClass.getClassLoader(), new Class[]{updaterClass},
                                          new UpdateProxy(mapper, updateBuilder));
    }

    public static UpdateProxy extract(Object updater) {
        return (UpdateProxy) getInvocationHandler(updater);
    }


    private UpdateProxy(Mapper<UpdateCommand> mapper, UpdateBuilder updateBuilder) {
        this.updateBuilder = updateBuilder;
        this.mapper = mapper;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        mapper.get(method).update(updateBuilder, args);
        return proxy;
    }

    public BasicDBObject getUpdateDocument() {
        return updateBuilder.getDocument();
    }

}
