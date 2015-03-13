package com.futureprocessing.documentjuggler.query;


import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.annotation.ObjectId;
import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
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

        final String field = FieldNameExtractor.getFieldName(method);

        if (method.isAnnotationPresent(ObjectId.class)) {
            return new IdQueryCommand(field);
        }

        return new BasicQueryCommand(field);
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
