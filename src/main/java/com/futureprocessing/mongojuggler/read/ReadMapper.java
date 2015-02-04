package com.futureprocessing.mongojuggler.read;


import com.futureprocessing.mongojuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.read.command.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ReadMapper {

    private static final Map<Class, Map<Method, ReadCommand>> mappings = new HashMap<>();

    public static Map<Method, ReadCommand> get(Class<?> clazz) {
        Map<Method, ReadCommand> map = mappings.get(clazz);
        if (map == null) {
            map = create(clazz);
            mappings.put(clazz, map);
        }
        return map;
    }

    private static Map<Method, ReadCommand> create(Class<?> clazz) {
        Map<Method, ReadCommand> map = new HashMap<>();

        for (Method method : clazz.getMethods()) {
            map.put(method, getCommand(method));
        }

        return map;
    }

    private static ReadCommand getCommand(Method method) {
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
