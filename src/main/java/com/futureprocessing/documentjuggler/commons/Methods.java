package com.futureprocessing.documentjuggler.commons;

import java.lang.reflect.Method;

public abstract class Methods {
    public static final Method EQUALS_METHOD = getEqualsMethod();

    private static Method getEqualsMethod() {
        try {
            return Object.class.getMethod("equals", Object.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Problem when initializing " + Methods.class.getName(), e);
        }
    }
}
