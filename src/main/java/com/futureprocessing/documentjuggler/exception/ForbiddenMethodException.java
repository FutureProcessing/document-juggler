package com.futureprocessing.documentjuggler.exception;

import com.futureprocessing.documentjuggler.Context;

import java.lang.reflect.Method;

public class ForbiddenMethodException extends RuntimeException {

    private final Method method;

    public ForbiddenMethodException(Method method, Context context) {
        super(String.format("Method %s is forbidden in %s", method.getName(), context));
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }
}
