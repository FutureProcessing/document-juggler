package com.futureprocessing.mongojuggler.commons;

import com.futureprocessing.mongojuggler.annotation.DbDocument;
import com.futureprocessing.mongojuggler.annotation.DbField;

import java.lang.reflect.Method;

public class Metadata {

    public static String getFieldName(Method method) {
        DbField field = method.getAnnotation(DbField.class);
        return field.value();
    }

    public static String getCollectionName(Class<?> clazz) {
        return clazz.getAnnotation(DbDocument.class).value();
    }



}
