package com.futureprocessing.documentjuggler.insert;


import com.futureprocessing.documentjuggler.annotation.AddToSet;
import com.futureprocessing.documentjuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.documentjuggler.annotation.Push;
import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.commons.Metadata;
import com.futureprocessing.documentjuggler.insert.command.*;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public final class InserterMapper extends Mapper<InsertCommand> {

    public InserterMapper(Class clazz) {
        super(clazz);
    }

    @Override
    protected InsertCommand getCommand(Method method) {
        String field = Metadata.getFieldName(method);

        if (!hasCorrectParameters(method) || method.isAnnotationPresent(AddToSet.class) || method.isAnnotationPresent(Push.class)) {
            return new UnsupportedInsertCommand(method);
        }

        if (method.isAnnotationPresent(DbEmbeddedDocument.class)) {
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
