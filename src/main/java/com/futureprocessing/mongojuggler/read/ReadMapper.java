package com.futureprocessing.mongojuggler.read;


import com.futureprocessing.mongojuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.commons.Mapper;
import com.futureprocessing.mongojuggler.read.command.*;

import java.lang.reflect.Method;
import java.util.Set;

public final class ReadMapper extends Mapper<ReadCommand> {

    public static final ReadMapper INSTANCE = new ReadMapper();

    @Override
    protected ReadCommand getCommand(Method method) {
        String field = getFieldName(method);

        if (method.isAnnotationPresent(DbEmbeddedDocument.class)) {
            return new EmbeddedReadCommand(field, method.getReturnType());
        }

        if (isBooleanReturnType(method)) {
            return new BooleanReadCommand(field);
        }

        if (isSetReturnType(method)) {
            return new SetReadCommand(field);
        }

        if (field.equals("_id")) {
            return new IdReadCommand();
        }

        return new BasicReadCommand(field);
    }

    private static String getFieldName(Method method) {
        DbField field = method.getAnnotation(DbField.class);
        return field.value();
    }

    private static boolean isSetReturnType(Method method) {
        return Set.class.isAssignableFrom(method.getReturnType());
    }

    private static boolean isBooleanReturnType(Method method) {
        return method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.class);
    }

    private ReadMapper() {
    }
}
