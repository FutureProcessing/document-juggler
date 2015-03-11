package com.futureprocessing.documentjuggler.commons;

import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.annotation.Id;

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
