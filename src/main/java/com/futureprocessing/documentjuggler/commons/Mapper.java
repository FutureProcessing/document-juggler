package com.futureprocessing.documentjuggler.commons;


import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public interface Mapper<COMMAND_TYPE> {
    Map<Method, COMMAND_TYPE> get();

    void createMapping(Class<?> clazz);

    void createEmbeddedMapping(String field, Class<?> embeddedType);

    Set<String> getSupportedFields();
}
