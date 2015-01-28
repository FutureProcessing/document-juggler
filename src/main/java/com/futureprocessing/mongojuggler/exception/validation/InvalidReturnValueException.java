package com.futureprocessing.mongojuggler.exception.validation;


import java.lang.reflect.Method;

import static java.lang.String.format;

public class InvalidReturnValueException extends InvalidModelInterfaceException {

    private final Method method;

    public InvalidReturnValueException(Method method) {
        super(format("Method %s return %s, only String or void are allowed", method, method.getReturnType().getName()));
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }
}
