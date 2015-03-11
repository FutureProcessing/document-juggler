package com.futureprocessing.mongojuggler.exception.validation;

import java.lang.reflect.Method;

public class UnsupportedMethodException extends RuntimeException {

    private final Method method;

    public UnsupportedMethodException(Method method) {
        super(String.format("Method %s is illegal in this model", method.getName()));
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }
}
