package com.futureprocessing.documentjuggler.insert;


import com.futureprocessing.documentjuggler.commons.ForbiddenChecker;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.insert.command.ForbiddenInsertCommand;
import com.futureprocessing.documentjuggler.insert.command.InsertCommand;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.Context.INSERT;

public final class InsertMapper extends Mapper<InsertCommand> {

    public static <MODEL> InsertMapper map(Class<MODEL> modelClass) {
        InsertMapper mapper = new InsertMapper();
        mapper.createMapping(modelClass);
        return mapper;
    }


    private InsertMapper() {
        super(INSERT, new DefaultInsertCommandProvider(), new ForbiddenInsertCommand.Provider());
    }

    @Override
    protected boolean isForbidden(Method method) {
        return ForbiddenChecker.isForbidden(method, INSERT) || !hasParameters(method);
    }

    private boolean hasParameters(Method method) {
        return method.getParameterCount() > 0;
    }


}
