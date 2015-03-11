package com.futureprocessing.documentjuggler.update.command;

import com.futureprocessing.documentjuggler.exception.validation.UnsupportedMethodException;
import com.futureprocessing.documentjuggler.update.UpdateBuilder;

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
