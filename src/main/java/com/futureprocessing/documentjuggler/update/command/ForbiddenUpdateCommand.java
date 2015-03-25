package com.futureprocessing.documentjuggler.update.command;


import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.exception.ForbiddenOperationException;
import com.futureprocessing.documentjuggler.update.UpdateBuilder;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.Context.UPDATE;

public class ForbiddenUpdateCommand implements UpdateCommand {

    private final Method method;

    public ForbiddenUpdateCommand(Method method) {
        this.method = method;
    }

    @Override
    public void update(UpdateBuilder updateBuilder, Object[] args) {
        throw new ForbiddenOperationException(method, UPDATE);
    }

    public static class Provider implements CommandProvider<UpdateCommand> {

        @Override
        public UpdateCommand getCommand(Method method, Mapper<UpdateCommand> mapper) {
            return new ForbiddenUpdateCommand(method);
        }
    }
}
