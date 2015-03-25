package com.futureprocessing.documentjuggler.read;


import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.read.command.ForbiddenReadCommand;
import com.futureprocessing.documentjuggler.read.command.ReadCommand;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.Context.READ;

public final class ReadMapper extends Mapper<ReadCommand> {

    public static <MODEL> ReadMapper map(Class<MODEL> modelClass) {
        ReadMapper mapper = new ReadMapper();
        mapper.createMapping(modelClass);
        return mapper;
    }

    private ReadMapper() {
        super(READ, new DefaultReadCommandProvider(), new ForbiddenReadCommand.Provider());
    }

    @Override
    protected boolean isForbidden(Method method) {
        return !hasCorrectParameters(method);
    }

    private boolean hasCorrectParameters(Method method) {
        return method.getParameterCount() == 0;
    }
}
