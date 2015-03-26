package com.futureprocessing.documentjuggler.commons;


import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public class ArgumentsReader {

    private final Method method;

    public static ArgumentsReader from(Method method) {
        return new ArgumentsReader(method);
    }

    private ArgumentsReader(Method method) {
        this.method = method;
    }

    public Class<?> getGenericType(int index) {
        return method.isVarArgs() ? getVarArgGenericType(index) : getSingleGenericType(index);
    }

    private Class<?> getSingleGenericType(int index) {
        ParameterizedType type = (ParameterizedType) method.getGenericParameterTypes()[0];
        return (Class<?>) type.getActualTypeArguments()[0];
    }

    private Class<?> getVarArgGenericType(int index) {
        GenericArrayType type = (GenericArrayType) method.getGenericParameterTypes()[0];
        return (Class<?>) ((ParameterizedType) type.getGenericComponentType()).getActualTypeArguments()[0];
    }
}
