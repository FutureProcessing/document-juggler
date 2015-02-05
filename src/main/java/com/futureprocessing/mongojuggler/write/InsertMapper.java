package com.futureprocessing.mongojuggler.write;


import com.futureprocessing.mongojuggler.annotation.AddToSet;
import com.futureprocessing.mongojuggler.annotation.DbEmbeddedDocument;
import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.commons.ProxyCreator;
import com.futureprocessing.mongojuggler.exception.UnsupportedActionException;
import com.futureprocessing.mongojuggler.read.command.*;
import com.futureprocessing.mongojuggler.write.command.BasicInsertCommand;
import com.futureprocessing.mongojuggler.write.command.EmbeddedInsertCommand;
import com.futureprocessing.mongojuggler.write.command.InsertCommand;
import com.futureprocessing.mongojuggler.write.command.UnsupportedInsertCommand;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static com.futureprocessing.mongojuggler.commons.ProxyExtractor.extractInsertEmbeddedProxy;

public final class InsertMapper {

    private static final Map<Class, Map<Method, InsertCommand>> mappings = new HashMap<>();

    public static Map<Method, InsertCommand> get(Class<?> clazz) {
        Map<Method, InsertCommand> map = mappings.get(clazz);
        if (map == null) {
            map = create(clazz);
            mappings.put(clazz, map);
        }
        return map;
    }

    private static Map<Method, InsertCommand> create(Class<?> clazz) {
        Map<Method, InsertCommand> map = new HashMap<>();

        for (Method method : clazz.getMethods()) {
            map.put(method, getCommand(method));
        }

        return map;
    }

    private static InsertCommand getCommand(Method method) {
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
