package com.futureprocessing.documentjuggler.commons;


import com.futureprocessing.documentjuggler.Context;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static com.futureprocessing.documentjuggler.annotation.AnnotationReader.from;
import static com.futureprocessing.documentjuggler.commons.Validator.validateField;
import static com.futureprocessing.documentjuggler.commons.Validator.validateInterface;

public interface Mapper<COMMAND_TYPE> {
    Map<Method, COMMAND_TYPE> get();

    void createMapping(Class<?> clazz);

    void createMapping(Optional<String> field, Class<?> clazz, Mapper<COMMAND_TYPE> mapper);

    Set<String> getSupportedFields();
}
