package com.futureprocessing.documentjuggler.commons;

import com.futureprocessing.documentjuggler.annotation.DbField;

import java.lang.reflect.Method;

public abstract class FieldNameExtractor {

    public static String getFieldName(Method method) {
        DbField field = method.getAnnotation(DbField.class);
        return field.value();
    }
}
