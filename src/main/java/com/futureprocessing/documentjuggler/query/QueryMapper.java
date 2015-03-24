package com.futureprocessing.documentjuggler.query;


import com.futureprocessing.documentjuggler.annotation.AsObjectId;
import com.futureprocessing.documentjuggler.annotation.*;
import com.futureprocessing.documentjuggler.annotation.internal.QueryContext;
import com.futureprocessing.documentjuggler.annotation.internal.UpdateContext;
import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.query.command.*;
import com.futureprocessing.documentjuggler.query.command.providers.DefaultQueryCommandProvider;
import com.futureprocessing.documentjuggler.update.command.UpdateCommand;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.Context.QUERY;
import static com.futureprocessing.documentjuggler.annotation.AnnotationReader.from;
import static com.futureprocessing.documentjuggler.commons.ForbiddenChecker.isForbidden;

public class QueryMapper extends Mapper<QueryCommand> {

    public static <MODEL> QueryMapper map(Class<MODEL> modelClass) {
        QueryMapper mapper = new QueryMapper(modelClass);
        mapper.createMapping(modelClass);
        return mapper;
    }

    private QueryMapper(Class clazz) {
        super(clazz, new DefaultQueryCommandProvider());
    }

    @Override
    protected QueryCommand getCommand(Method method) {
        AnnotationReader reader = from(method);

        if (isForbidden(method, QUERY) || !hasCorrectReturnType(method) || !hasCorrectParameters(method)) {
            return new ForbiddenQueryCommand(method);
        }

        QueryContext context = from(method).read(QueryContext.class);
        if (context != null) {
            Class<? extends CommandProvider<QueryCommand>> commandClass = context.commandProvider();

            try {
                CommandProvider<QueryCommand> commandProvider = commandClass.newInstance();
                return commandProvider.getCommand(method, this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return getDefaultCommand(method);
    }

    private boolean hasCorrectReturnType(Method method) {
        Class<?> returnType = method.getReturnType();
        Class<?> model = method.getDeclaringClass();

        return model.isAssignableFrom(returnType) || returnType.equals(Void.TYPE);
    }

    private boolean hasCorrectParameters(Method method) {
        return method.getParameterCount() == 1;
    }
}
