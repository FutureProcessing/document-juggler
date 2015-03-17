package com.futureprocessing.documentjuggler.update.command;


import com.futureprocessing.documentjuggler.exception.ForbiddenActionException;
import com.futureprocessing.documentjuggler.update.UpdateBuilder;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.Context.INSERT;
import static com.futureprocessing.documentjuggler.Context.UPDATE;

public class ForbiddenUpdateCommand implements UpdateCommand {

    private final Method method;

    public ForbiddenUpdateCommand(Method method) {
        this.method = method;
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        throw new ForbiddenActionException(method, UPDATE);
    }
}
