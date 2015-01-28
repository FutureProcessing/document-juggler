package com.futureprocessing.mongojuggler.read;


import com.futureprocessing.mongojuggler.annotation.DbField;
import com.futureprocessing.mongojuggler.annotation.Id;
import com.futureprocessing.mongojuggler.exception.validation.InvalidArgumentsException;
import com.futureprocessing.mongojuggler.exception.validation.InvalidReturnValueException;
import com.futureprocessing.mongojuggler.exception.validation.ModelIsNotInterfaceException;
import com.futureprocessing.mongojuggler.exception.validation.UnknownFieldException;

import java.lang.reflect.Method;

public final class QueryValidator {

    public static void validate(Class<?> query) {
        validateInterface(query);

        for (Method method : query.getMethods()) {
            validateField(method);
            validateReturnType(method);
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

    private static void validateReturnType(Method method) {
        Class<?> returnType = method.getReturnType();
        Class<?> clazz = method.getDeclaringClass();

        if (!returnType.equals(clazz) && !returnType.equals(Void.TYPE)) {
            throw new InvalidReturnValueException(method);
        }
    }

    private static void validateArguments(Method method) {
        if (method.getParameterCount() != 1) {
            throw new InvalidArgumentsException(method);
        }
    }


    private QueryValidator() {
    }
}
