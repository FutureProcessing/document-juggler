package com.futureprocessing.documentjuggler.commons;


import java.lang.reflect.Method;
import java.util.Set;

public interface Mapper<COMMAND_TYPE> {

    COMMAND_TYPE get(Method method);

    void createMapping(Class<?> clazz);

    void createEmbeddedMapping(String field, Class<?> embeddedType);

    Set<String> getSupportedFields();
}
