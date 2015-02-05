package com.futureprocessing.mongojuggler.write;

import com.futureprocessing.mongojuggler.write.command.UpdateCommand;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class UpdateProxy implements InvocationHandler {

    private final UpdateBuilder updateBuilder;
    private final Map<Method, UpdateCommand> updateCommands;

    public UpdateProxy(Class<?> clazz, UpdateBuilder updateBuilder) {
        this.updateBuilder = updateBuilder;
        updateCommands = UpdateMapper.INSTANCE.get(clazz);
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
