package com.futureprocessing.documentjuggler.query;


import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.annotation.Id;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.query.command.BasicQueryCommand;
import com.futureprocessing.documentjuggler.query.command.IdQueryCommand;
import com.futureprocessing.documentjuggler.query.command.QueryCommand;
import com.futureprocessing.documentjuggler.query.command.UnsupportedQueryCommand;

import java.lang.reflect.Method;

public class QueryMapper extends Mapper<QueryCommand> {

    public QueryMapper(Class clazz) {
        super(clazz);
    }

    @Override
    protected QueryCommand getCommand(Method method) {

        if (!hasCorrectReturnType(method) || !hasCorrectParameters(method)) {
            return new UnsupportedQueryCommand(method);
        }

        if (method.isAnnotationPresent(Id.class)) {
            return new IdQueryCommand();
        }

        String field = method.getAnnotation(DbField.class).value();
        return new BasicQueryCommand(field);
    }

    private boolean hasCorrectReturnType(Method method) {
        Class<?> returnType = method.getReturnType();
        Class<?> clazz = method.getDeclaringClass();

        return returnType.equals(clazz) || returnType.equals(Void.TYPE);
    }

    private boolean hasCorrectParameters(Method method) {
        return method.getParameterCount() == 1;
    }
}
