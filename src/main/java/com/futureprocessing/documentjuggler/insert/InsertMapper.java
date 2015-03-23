package com.futureprocessing.documentjuggler.insert;


import com.futureprocessing.documentjuggler.annotation.*;
import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.insert.command.*;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import static com.futureprocessing.documentjuggler.Context.INSERT;
import static com.futureprocessing.documentjuggler.annotation.AnnotationReader.from;
import static com.futureprocessing.documentjuggler.commons.ForbiddenChecker.isForbidden;

public final class InsertMapper extends Mapper<InsertCommand> {

    public InsertMapper(Class clazz) {
        super(clazz);
    }

    @Override
    protected InsertCommand getCommand(Method method) {
        AnnotationReader annotationReader = from(method);
        String field = FieldNameExtractor.getFieldName(method);

        if (isForbidden(method, INSERT) || !hasCorrectParameters(method)
                || annotationReader.isPresent(AddToSet.class) || annotationReader.isPresent(Push.class)) {
            return new ForbiddenInsertCommand(method);
        }

        if (annotationReader.isPresent(DbEmbeddedDocument.class)) {
            if (method.isVarArgs()) {
                Class<?> type = getEmbeddedListDocumentType(method);
                createMapping(type);
                return new EmbeddedVarArgInsertCommand(field, type, this);
            }

            Class<?> type = getEmbeddedDocumentType(method);
            createMapping(type);
            return new EmbeddedInsertCommand(field, type, this);
        }

        if (annotationReader.isPresent(AsObjectId.class)){
             if(!method.getParameterTypes()[0].equals(String.class)){
                return new ForbiddenInsertCommand(method);
            }

            return new IdInsertCommand(field);
        }

        return new BasicInsertCommand(field);
    }

    private boolean hasCorrectParameters(Method method) {
        return method.getParameterCount() > 0;
    }

    private Class<?> getEmbeddedDocumentType(Method method) {
        ParameterizedType type = (ParameterizedType) method.getGenericParameterTypes()[0];
        return (Class<?>) type.getActualTypeArguments()[0];
    }

    private Class<?> getEmbeddedListDocumentType(Method method) {
        GenericArrayType type = (GenericArrayType) method.getGenericParameterTypes()[0];
        return (Class<?>) ((ParameterizedType) type.getGenericComponentType()).getActualTypeArguments()[0];
    }

}
