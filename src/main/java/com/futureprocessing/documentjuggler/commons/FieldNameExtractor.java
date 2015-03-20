package com.futureprocessing.documentjuggler.commons;

import com.futureprocessing.documentjuggler.annotation.DbField;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.annotation.AnnotationReader.from;

public abstract class FieldNameExtractor {

    public static String getFieldName(Method method) {
        DbField field = from(method).read(DbField.class);
        return field.value();
    }
}
