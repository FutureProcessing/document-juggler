package com.futureprocessing.mongojuggler.commons;


import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.annotation.Id;
import com.futureprocessing.mongojuggler.exception.validation.ModelIsNotInterfaceException;
import com.futureprocessing.mongojuggler.exception.validation.UnknownFieldException;

import java.lang.reflect.Method;

public final class Validator {

    public static void validateInterface(Class<?> model) {
        if (!model.isInterface()) {
            throw new ModelIsNotInterfaceException(model);
        }
    }

    public static void validateField(Method method) {
        if (!method.isAnnotationPresent(DbField.class) && !method.isAnnotationPresent(Id.class)) {
            throw new UnknownFieldException(method);
        }
    }

    private Validator() {
    }
}
