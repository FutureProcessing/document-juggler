package com.futureprocessing.documentjuggler.commons;

import com.futureprocessing.documentjuggler.annotation.DbField;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.futureprocessing.documentjuggler.annotation.AnnotationReader.from;

public abstract class FieldNameExtractor {

    public static String getFieldName(Method method) {
        DbField field = from(method).read(DbField.class);
        if (field != null) {
            return field.value();
        }

        List<String> toRemove = Arrays.asList("get", "set", "with");

        String methodName = method.getName();
        for (String remove : toRemove) {
            if (methodName.startsWith(remove)) {
                String result = methodName.replaceFirst(remove, "");
                return firstLetterToLowerCase(result);
            }
        }

        return null;
    }

    private static String firstLetterToLowerCase(String result) {
        return result.substring(0, 1).toLowerCase() + result.substring(1);
    }
}
