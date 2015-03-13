package com.futureprocessing.documentjuggler.read;


import com.futureprocessing.documentjuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.documentjuggler.annotation.ObjectId;
import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.read.command.*;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;

public final class ReaderMapper extends Mapper<ReadCommand> {

    public ReaderMapper(Class clazz) {
        super(clazz);
    }

    @Override
    protected ReadCommand getCommand(Method method) {
        if (!hasCorrectParameters(method)) {
            return new UnsupportedReadCommand(method);
        }

        String field = FieldNameExtractor.getFieldName(method);

        if (method.isAnnotationPresent(DbEmbeddedDocument.class)) {
            Class<?> returnType = method.getReturnType();

            if (returnType.equals(List.class)) {
                Class embeddedType = (Class) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
                createMapping(embeddedType);
                return new EmbeddedListReadCommand(field, embeddedType, this);
            }

            if (returnType.equals(Set.class)) {
                Class embeddedType = (Class) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
                createMapping(embeddedType);
                return new EmbeddedSetReadCommand(field, embeddedType, this);
            }

            createMapping(returnType);
            return new EmbeddedReadCommand(field, method.getReturnType(), this);
        }

        if (isBooleanReturnType(method)) {
            return new BooleanReadCommand(field);
        }

        if (isSetReturnType(method)) {
            return new SetReadCommand(field);
        }

        if (method.isAnnotationPresent(ObjectId.class)) {
            return new IdReadCommand(field);
        }

        return new BasicReadCommand(field);
    }

    private boolean hasCorrectParameters(Method method) {
        return method.getParameterCount() == 0;
    }

    private boolean isSetReturnType(Method method) {
        return Set.class.isAssignableFrom(method.getReturnType());
    }

    private boolean isBooleanReturnType(Method method) {
        return method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.class);
    }

}
