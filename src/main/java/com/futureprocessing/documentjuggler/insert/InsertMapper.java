package com.futureprocessing.documentjuggler.insert;


import com.futureprocessing.documentjuggler.commons.AbstractMapper;
import com.futureprocessing.documentjuggler.insert.command.ForbiddenInsertCommand;
import com.futureprocessing.documentjuggler.insert.command.InsertCommand;
import com.futureprocessing.documentjuggler.query.operators.Comparison;
import com.futureprocessing.documentjuggler.update.operators.Update;
import com.futureprocessing.documentjuggler.update.operators.UpdateArrays;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.Context.INSERT;

public final class InsertMapper extends AbstractMapper<InsertCommand> {

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
        return !hasParameters(method) || hasParameterOfType(method, Comparison.class, Update.class, UpdateArrays.class);
    }

    private boolean hasParameters(Method method) {
        return method.getParameterCount() > 0;
    }


}
