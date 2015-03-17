package com.futureprocessing.documentjuggler.exception;

import com.futureprocessing.documentjuggler.Context;

import java.lang.reflect.Method;

public class ForbiddenActionException extends RuntimeException {

    private final Method method;
    private final Context context;

    public ForbiddenActionException(Method method, Context context) {
        super(String.format("Method %s is forbidden in %s", method.getName(), context));
        this.method = method;
        this.context = context;
    }

    public Method getMethod() {
        return method;
    }

    public Context getContext() {
        return context;
    }
}
