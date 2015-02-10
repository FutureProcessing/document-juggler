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
    public static <UPDATER> UPDATER create(Class<UPDATER> updaterClass, UpdateMapper mapper, UpdateBuilder updateBuilder) {
        return (UPDATER) newProxyInstance(updaterClass.getClassLoader(), new Class[]{updaterClass},
                new UpdateProxy(updaterClass, updateBuilder, mapper));
    }

    public static UpdateProxy extract(Object updater) {
        return (UpdateProxy) getInvocationHandler(updater);
    }


    private UpdateProxy(Class<?> clazz, UpdateBuilder updateBuilder, UpdateMapper mapper) {
        this.updateBuilder = updateBuilder;
        updateCommands = mapper.get(clazz);
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
