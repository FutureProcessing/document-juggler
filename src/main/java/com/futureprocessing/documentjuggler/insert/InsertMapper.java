package com.futureprocessing.documentjuggler.insert;


import com.futureprocessing.documentjuggler.annotation.internal.InsertContext;
import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.insert.command.*;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.Context.INSERT;
import static com.futureprocessing.documentjuggler.annotation.AnnotationReader.from;
import static com.futureprocessing.documentjuggler.commons.ForbiddenChecker.isForbidden;

public final class InsertMapper extends Mapper<InsertCommand> {

    public static <MODEL> InsertMapper map(Class<MODEL> modelClass) {
        InsertMapper mapper = new InsertMapper(modelClass);
        mapper.createMapping(modelClass);
        return mapper;
    }


    private InsertMapper(Class clazz) {
        super(clazz, new DefaultInsertCommandProvider());
    }

    @Override
    protected InsertCommand getCommand(Method method) {

        if (isForbidden(method, INSERT) || !hasParameters(method)) {
            return new ForbiddenInsertCommand(method);
        }

        InsertContext context = from(method).read(InsertContext.class);
        if (context != null) {
            Class<? extends CommandProvider<InsertCommand>> commandClass = context.commandProvider();

            try {
                CommandProvider<InsertCommand> commandProvider = commandClass.newInstance();
                return commandProvider.getCommand(method, this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return getDefaultCommand(method);
    }

    private boolean hasParameters(Method method) {
        return method.getParameterCount() > 0;
    }


}
