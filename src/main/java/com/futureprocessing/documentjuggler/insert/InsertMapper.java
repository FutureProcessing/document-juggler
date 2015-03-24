package com.futureprocessing.documentjuggler.insert;


import com.futureprocessing.documentjuggler.commons.ForbiddenChecker;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.insert.command.*;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.Context.INSERT;

public final class InsertMapper extends Mapper<InsertCommand> {

    public static <MODEL> InsertMapper map(Class<MODEL> modelClass) {
        InsertMapper mapper = new InsertMapper(modelClass);
        mapper.createMapping(modelClass);
        return mapper;
    }


    private InsertMapper(Class clazz) {
        super(INSERT, new DefaultInsertCommandProvider());
    }

    @Override
    protected InsertCommand getForbidden(Method method) {

        if (ForbiddenChecker.isForbidden(method, INSERT) || !hasParameters(method)) {
            return new ForbiddenInsertCommand(method);
        }

        return null;
    }

    private boolean hasParameters(Method method) {
        return method.getParameterCount() > 0;
    }


}
