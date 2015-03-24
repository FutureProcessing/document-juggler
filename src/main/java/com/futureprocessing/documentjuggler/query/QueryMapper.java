package com.futureprocessing.documentjuggler.query;


import com.futureprocessing.documentjuggler.annotation.AsObjectId;
import com.futureprocessing.documentjuggler.annotation.*;
import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.query.command.*;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.Context.QUERY;
import static com.futureprocessing.documentjuggler.annotation.AnnotationReader.from;
import static com.futureprocessing.documentjuggler.commons.ForbiddenChecker.isForbidden;

public class QueryMapper extends Mapper<QueryCommand> {

    public QueryMapper(Class clazz) {
        super(clazz, null);
        createMapping(clazz);
    }

    @Override
    protected QueryCommand getCommand(Method method) {
        AnnotationReader reader = from(method);

        if (isForbidden(method, QUERY) || !hasCorrectReturnType(method) || !hasCorrectParameters(method)) {
            return new ForbiddenQueryCommand(method);
        }

        final String field = FieldNameExtractor.getFieldName(method);

        if (reader.isPresent(AsObjectId.class)) {
            return new IdQueryCommand(field);
        }

        if (reader.isPresent(GreaterThan.class)) {
            return new GreaterThanQueryCommand(field);
        }

        if (reader.isPresent(GreaterThanEqual.class)) {
            return new GreaterThanEqualQueryCommand(field);
        }

        if (reader.isPresent(LessThan.class)) {
            return new LessThanQueryCommand(field);
        }

        if (reader.isPresent(LessThanEqual.class)) {
            return new LessThanEqualQueryCommand(field);
        }

        if (reader.isPresent(Exists.class)) {
            return new ExistsQueryCommand(field);
        }

        if (reader.isPresent(In.class)) {
            return new InQueryCommand(field);
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
