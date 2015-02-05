package com.futureprocessing.mongojuggler.write;


import com.futureprocessing.mongojuggler.annotation.AddToSet;
import com.futureprocessing.mongojuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.commons.Mapper;
import com.futureprocessing.mongojuggler.write.command.BasicInsertCommand;
import com.futureprocessing.mongojuggler.write.command.EmbeddedInsertCommand;
import com.futureprocessing.mongojuggler.write.command.InsertCommand;
import com.futureprocessing.mongojuggler.write.command.UnsupportedInsertCommand;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public final class InsertMapper extends Mapper<InsertCommand> {

    public static final InsertMapper INSTANCE = new InsertMapper();

    @Override
    protected InsertCommand getCommand(Method method) {
        String field = getFieldName(method);

        if (method.isAnnotationPresent(AddToSet.class)) {
            return new UnsupportedInsertCommand();
        }

        if (method.isAnnotationPresent(DbEmbeddedDocument.class)) {
            Class<?> type = getEmbeddedDocumentType(method);
            return new EmbeddedInsertCommand(field, type);
        }

        return new BasicInsertCommand(field);
    }

    private static String getFieldName(Method method) {
        DbField field = method.getAnnotation(DbField.class);
        return field.value();
    }

    private static Class<?> getEmbeddedDocumentType(Method method) {
        ParameterizedType type = (ParameterizedType) method.getGenericParameterTypes()[0];
        return (Class<?>) type.getActualTypeArguments()[0];
    }

    private InsertMapper() {
    }
}
