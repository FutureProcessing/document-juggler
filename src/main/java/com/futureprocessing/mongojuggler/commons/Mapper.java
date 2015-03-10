package com.futureprocessing.mongojuggler.commons;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.futureprocessing.mongojuggler.commons.Validator.validateInterface;

public abstract class Mapper<COMMAND_TYPE> {

    private final Set<Class> mappedClasses = new HashSet<>();
    private final Map<Method, COMMAND_TYPE> mappings = new HashMap<>();
    private final boolean strictMode;

    protected Mapper(Class clazz, boolean strictMode) {
        this.strictMode = strictMode;
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
                mappings.put(method, getCommand(method));
            }

            mappedClasses.add(clazz);
        }
    }

    protected abstract COMMAND_TYPE getCommand(Method method);

    protected boolean isStrictMode() {
        return strictMode;
    }
}
