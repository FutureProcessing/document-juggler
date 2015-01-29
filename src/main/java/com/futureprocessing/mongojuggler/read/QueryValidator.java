package com.futureprocessing.mongojuggler.read;


import com.futureprocessing.mongojuggler.exception.validation.InvalidArgumentsException;
import com.futureprocessing.mongojuggler.exception.validation.InvalidReturnValueException;

import java.lang.reflect.Method;

import static com.futureprocessing.mongojuggler.commons.Validator.validateField;
import static com.futureprocessing.mongojuggler.commons.Validator.validateInterface;

public final class QueryValidator {

    public static void validate(Class<?> query) {
        validateInterface(query);

        for (Method method : query.getMethods()) {
            validateField(method);
            validateReturnType(method);
            validateArguments(method);
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
