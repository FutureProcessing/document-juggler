package com.futureprocessing.mongojuggler.commons;


import com.futureprocessing.mongojuggler.MappingMode;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.futureprocessing.mongojuggler.commons.Validator.validateField;
import static com.futureprocessing.mongojuggler.commons.Validator.validateInterface;

public abstract class Mapper<COMMAND_TYPE> {

    private final Set<Class> mappedClasses = new HashSet<>();
    private final Map<Method, COMMAND_TYPE> mappings = new HashMap<>();
    private final MappingMode mappingMode;

    protected Mapper(Class clazz, MappingMode mappingMode) {
        this.mappingMode = mappingMode;
        createMapping(clazz);
    }

    public final Map<Method, COMMAND_TYPE> get() {
        return mappings;
    }

    public COMMAND_TYPE get(Method method) {
        return mappings.get(method);
    }

    protected void createMapping(Class<?> clazz) {
        if (!mappedClasses.contains(clazz)) {
            validateInterface(clazz);

            for (Method method : clazz.getMethods()) {
                validateField(method);
                mappings.put(method, getCommand(method));
            }

            mappedClasses.add(clazz);
        }
    }

    protected abstract COMMAND_TYPE getCommand(Method method);

    protected boolean isStrictMode() {
        return mappingMode == MappingMode.STRICT;
    }
}
