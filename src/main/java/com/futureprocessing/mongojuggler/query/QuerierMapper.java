package com.futureprocessing.mongojuggler.query;


import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.annotation.Id;
import com.futureprocessing.mongojuggler.commons.Mapper;
import com.futureprocessing.mongojuggler.query.command.BasicQueryCommand;
import com.futureprocessing.mongojuggler.query.command.IdQueryCommand;
import com.futureprocessing.mongojuggler.query.command.QueryCommand;
import com.futureprocessing.mongojuggler.query.command.UnsupportedQueryCommand;

import java.lang.reflect.Method;

public class QuerierMapper extends Mapper<QueryCommand> {

    public QuerierMapper(Class clazz) {
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
