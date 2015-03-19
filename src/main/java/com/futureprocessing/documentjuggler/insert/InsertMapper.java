package com.futureprocessing.documentjuggler.insert;


import com.futureprocessing.documentjuggler.annotation.AddToSet;
import com.futureprocessing.documentjuggler.annotation.AnnotationReader;
import com.futureprocessing.documentjuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.documentjuggler.annotation.Push;
import com.futureprocessing.documentjuggler.commons.FieldNameExtractor;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.insert.command.*;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import static com.futureprocessing.documentjuggler.Context.INSERT;
import static com.futureprocessing.documentjuggler.annotation.AnnotationReader.process;
import static com.futureprocessing.documentjuggler.commons.ForbiddenChecker.isForbidden;

public final class InsertMapper extends Mapper<InsertCommand> {

    public InsertMapper(Class clazz) {
        super(clazz);
    }

    @Override
    protected InsertCommand getCommand(Method method) {
        AnnotationReader annotationReader = process(method);
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
