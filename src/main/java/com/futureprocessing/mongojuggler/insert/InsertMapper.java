package com.futureprocessing.mongojuggler.insert;


import com.futureprocessing.mongojuggler.annotation.AddToSet;
import com.futureprocessing.mongojuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.annotation.Push;
import com.futureprocessing.mongojuggler.commons.Mapper;
import com.futureprocessing.mongojuggler.insert.command.*;

import java.lang.reflect.*;

public final class InsertMapper extends Mapper<InsertCommand> {

    public InsertMapper(Class clazz) {
        super(clazz);
    }

    @Override
    protected InsertCommand getCommand(Method method) {
        String field = getFieldName(method);

        if (method.isAnnotationPresent(AddToSet.class) || method.isAnnotationPresent(Push.class)) {
            return new UnsupportedInsertCommand();
        }

        if (method.isAnnotationPresent(DbEmbeddedDocument.class)) {
            if(method.isVarArgs()) {
                Class<?> type = getEmbeddedListDocumentType(method);
                createMapping(type);
                return new EmbeddedVarArgInsertCommand(field, type, this);
            }

            Class<?> type =  getEmbeddedDocumentType(method);
            createMapping(type);
            return new EmbeddedInsertCommand(field, type, this);
        }

        return new BasicInsertCommand(field);
    }

    private String getFieldName(Method method) {
        DbField field = method.getAnnotation(DbField.class);
        return field.value();
    }

    private Class<?> getEmbeddedDocumentType(Method method) {
        ParameterizedType type = (ParameterizedType) method.getGenericParameterTypes()[0];
        return (Class<?>) type.getActualTypeArguments()[0];
    }

    private Class<?> getEmbeddedListDocumentType(Method method) {
        GenericArrayType type = (GenericArrayType) method.getGenericParameterTypes()[0];
        return (Class<?>) ((ParameterizedType)type.getGenericComponentType()).getActualTypeArguments()[0];
    }

}
