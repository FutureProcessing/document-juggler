package com.futureprocessing.mongojuggler.exception.validation;


import java.lang.reflect.Method;

import static java.lang.String.format;

public class UnknownFieldException extends InvalidModelInterfaceException {

    private final Method method;

    public UnknownFieldException(Method method) {
        super(format("Unknown field for %s()", method.getName()));
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }
}
