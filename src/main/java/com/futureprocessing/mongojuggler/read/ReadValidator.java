package com.futureprocessing.mongojuggler.read;

import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.annotation.Id;
import com.futureprocessing.mongojuggler.exception.validation.InvalidArgumentsException;
import com.futureprocessing.mongojuggler.exception.validation.ModelIsNotInterfaceException;
import com.futureprocessing.mongojuggler.exception.validation.UnknownFieldException;

import java.lang.reflect.Method;

public final class ReadValidator {

    public static void validate(Class<?> reader) {
        validateInterface(reader);

        for (Method method : reader.getMethods()) {
            validateField(method);
            validateArguments(method);
        }
    }

    private static void validateInterface(Class<?> query) {
        if (!query.isInterface()) {
            throw new ModelIsNotInterfaceException(query);
        }
    }

    private static void validateField(Method method) {
        if (!method.isAnnotationPresent(DbField.class) && !method.isAnnotationPresent(Id.class)) {
            throw new UnknownFieldException(method);
        }
    }

    private static void validateArguments(Method method) {
        if (method.getParameterCount() != 0) {
            throw new InvalidArgumentsException(method);
        }
    }

    private ReadValidator() {}
}
