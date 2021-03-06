package com.futureprocessing.documentjuggler.commons;


import com.futureprocessing.documentjuggler.annotation.DbField;
import com.futureprocessing.documentjuggler.exception.validation.ModelIsNotInterfaceException;
import com.futureprocessing.documentjuggler.exception.validation.UnknownFieldException;

import java.lang.reflect.Method;

import static com.futureprocessing.documentjuggler.annotation.AnnotationReader.from;

public final class Validator {

    public static void validateInterface(Class<?> model) {
        if (!model.isInterface()) {
            throw new ModelIsNotInterfaceException(model);
        }
    }

    public static void validateField(Method method) {
        if (!from(method).isPresent(DbField.class)) {
            throw new UnknownFieldException(method);
        }
    }
}
