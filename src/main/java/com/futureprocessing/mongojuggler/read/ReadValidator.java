package com.futureprocessing.mongojuggler.read;

import com.futureprocessing.mongojuggler.exception.validation.InvalidArgumentsException;

import java.lang.reflect.Method;

import static com.futureprocessing.mongojuggler.commons.Validator.validateField;
import static com.futureprocessing.mongojuggler.commons.Validator.validateInterface;

public final class ReadValidator {

    public static void validate(Class<?> reader) {
        validateInterface(reader);

        for (Method method : reader.getMethods()) {
            validateField(method);
            validateArguments(method);
        }
    }

    private static void validateArguments(Method method) {
        if (method.getParameterCount() != 0) {
            throw new InvalidArgumentsException(method);
        }
    }

    private ReadValidator() {
    }
}
