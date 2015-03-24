package com.futureprocessing.documentjuggler.read;


import com.futureprocessing.documentjuggler.commons.ForbiddenChecker;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.read.command.ForbiddenReadCommand;
import com.futureprocessing.documentjuggler.read.command.ReadCommand;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.Context.READ;

public final class ReadMapper extends Mapper<ReadCommand> {

    public static <MODEL> ReadMapper map(Class<MODEL> modelClass) {
        ReadMapper mapper = new ReadMapper(modelClass);
        mapper.createMapping(modelClass);
        return mapper;
    }

    private ReadMapper(Class clazz) {
        super(READ, new DefaultReadCommandProvider());
    }

    @Override
    protected ReadCommand getForbidden(Method method) {
        if (ForbiddenChecker.isForbidden(method, READ) || !hasCorrectParameters(method)) {
            return new ForbiddenReadCommand(method);
        }

        return null;

    }

    private boolean hasCorrectParameters(Method method) {
        return method.getParameterCount() == 0;
    }
}
