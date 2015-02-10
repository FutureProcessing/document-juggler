package com.futureprocessing.mongojuggler.write;

import com.futureprocessing.mongojuggler.write.command.UpdateCommand;
import com.mongodb.BasicDBObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import static java.lang.reflect.Proxy.getInvocationHandler;
import static java.lang.reflect.Proxy.newProxyInstance;

public class UpdateProxy implements InvocationHandler {

    private final UpdateBuilder updateBuilder;
    private final Map<Method, UpdateCommand> updateCommands;

    @SuppressWarnings("unchecked")
    public static <UPDATER> UPDATER create(Class<UPDATER> updaterClass, Map<Method, UpdateCommand> updateCommands, UpdateBuilder updateBuilder) {
        return (UPDATER) newProxyInstance(updaterClass.getClassLoader(), new Class[]{updaterClass},
                new UpdateProxy(updateCommands, updateBuilder));
    }

    public static UpdateProxy extract(Object updater) {
        return (UpdateProxy) getInvocationHandler(updater);
    }


    private UpdateProxy(Map<Method, UpdateCommand> updateCommands, UpdateBuilder updateBuilder) {
        this.updateBuilder = updateBuilder;
        this.updateCommands = updateCommands;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        updateCommands.get(method).update(updateBuilder, args);
        return proxy;
    }

    public BasicDBObject getUpdateDocument() {
        return updateBuilder.getDocument();
    }

}
