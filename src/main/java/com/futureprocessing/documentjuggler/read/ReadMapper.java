package com.futureprocessing.documentjuggler.read;


import com.futureprocessing.documentjuggler.commons.AbstractMapper;
import com.futureprocessing.documentjuggler.query.operators.Comparison;
import com.futureprocessing.documentjuggler.read.command.ForbiddenReadCommand;
import com.futureprocessing.documentjuggler.read.command.ReadCommand;
import com.futureprocessing.documentjuggler.update.operators.Update;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.Context.READ;

public class ReadMapper extends AbstractMapper<ReadCommand> {

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
        return !hasCorrectParameters(method) || hasParameterOfType(method, Comparison.class, Update.class);
    }

    private boolean hasCorrectParameters(Method method) {
        return method.getParameterCount() == 0;
    }
}
