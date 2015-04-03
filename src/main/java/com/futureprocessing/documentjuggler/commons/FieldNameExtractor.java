package com.futureprocessing.documentjuggler.commons;

import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.exception.validation.UnknownFieldException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.futureprocessing.documentjuggler.annotation.AnnotationReader.from;

public abstract class FieldNameExtractor {

    public static String getFieldName(Method method) {
        DbField field = from(method).read(DbField.class);
        if (field != null && !field.value().isEmpty()) {
            return field.value();
        }

        List<String> toRemove = Arrays.asList("get", "set", "with");

        String methodName = method.getName();
        for (String prefix : toRemove) {
            if (methodName.startsWith(prefix)) {
                String result = methodName.replaceFirst(prefix, "");
                if (result.isEmpty()) {
                    break;
                }
                return firstLetterToLowerCase(result);
            }
        }
        throw new UnknownFieldException(method);
    }

    private static String firstLetterToLowerCase(String result) {
        return result.substring(0, 1).toLowerCase() + result.substring(1);
    }
}
