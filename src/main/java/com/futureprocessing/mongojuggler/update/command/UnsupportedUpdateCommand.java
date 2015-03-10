package com.futureprocessing.mongojuggler.update.command;

import com.futureprocessing.mongojuggler.exception.validation.UnsupportedMethodException;
import com.futureprocessing.mongojuggler.update.UpdateBuilder;

import java.lang.reflect.Method;

public class UnsupportedUpdateCommand implements UpdateCommand{

    private final Method method;

    public UnsupportedUpdateCommand(Method method) {
        this.method = method;
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        throw new UnsupportedMethodException(method);
    }
}
