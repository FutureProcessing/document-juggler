package com.futureprocessing.documentjuggler.commons;

import com.futureprocessing.documentjuggler.annotation.DbField;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.annotation.AnnotationProcessor.annotation;

public abstract class FieldNameExtractor {

    public static String getFieldName(Method method) {
        DbField field = annotation(DbField.class).getFrom(method);
        return field.value();
    }
}
