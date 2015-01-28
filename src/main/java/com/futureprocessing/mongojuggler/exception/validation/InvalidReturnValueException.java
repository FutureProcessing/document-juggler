package com.futureprocessing.mongojuggler.exception.validation;


import java.lang.reflect.Method;

public class InvalidReturnValueException extends InvalidModelInterfaceException {

    private final Method method;

    public InvalidReturnValueException(Method method) {
        super(String.format("Method %s return %s, only String or void are allowed", method, method.getReturnType().getName()));
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }
}
