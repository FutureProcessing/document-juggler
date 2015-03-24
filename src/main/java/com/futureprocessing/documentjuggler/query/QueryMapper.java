package com.futureprocessing.documentjuggler.query;


import com.futureprocessing.documentjuggler.commons.ForbiddenChecker;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.query.command.ForbiddenQueryCommand;
import com.futureprocessing.documentjuggler.query.command.QueryCommand;
import com.futureprocessing.documentjuggler.query.command.providers.DefaultQueryCommandProvider;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.Context.QUERY;

public class QueryMapper extends Mapper<QueryCommand> {

    public static <MODEL> QueryMapper map(Class<MODEL> modelClass) {
        QueryMapper mapper = new QueryMapper(modelClass);
        mapper.createMapping(modelClass);
        return mapper;
    }

    private QueryMapper(Class clazz) {
        super(QUERY, new DefaultQueryCommandProvider(), new ForbiddenQueryCommand.Provider());
    }

    @Override
    protected boolean isForbidden(Method method) {
        return ForbiddenChecker.isForbidden(method, QUERY) || !hasCorrectReturnType(method) || !hasCorrectParameters(method);
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
