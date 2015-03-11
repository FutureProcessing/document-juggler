package com.futureprocessing.documentjuggler.exception.validation;


import java.lang.reflect.Method;

import static java.lang.String.format;

public class InvalidArgumentsException extends InvalidModelInterfaceException {

    private final Method method;

    public InvalidArgumentsException(Method method) {
        super(format("Method %s has incorrect arguments", method));
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }
}
