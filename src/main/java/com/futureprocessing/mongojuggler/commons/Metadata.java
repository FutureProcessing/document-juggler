package com.futureprocessing.mongojuggler.commons;

import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.annotation.Id;

import java.lang.reflect.Method;

public class Metadata {

    public static String getFieldName(Method method) {
        if (method.isAnnotationPresent(Id.class)) {
            return "_id";
        }
        DbField field = method.getAnnotation(DbField.class);
        return field.value();
    }
}
