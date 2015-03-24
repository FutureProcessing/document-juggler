package com.futureprocessing.documentjuggler.commons;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.futureprocessing.documentjuggler.commons.Validator.validateField;
import static com.futureprocessing.documentjuggler.commons.Validator.validateInterface;

public abstract class Mapper<COMMAND_TYPE> {

    private final Set<Class> mappedClasses = new HashSet<>();
    private final Map<Method, COMMAND_TYPE> mappings = new HashMap<>();
    private final CommandProvider<COMMAND_TYPE> defaultCommandProvider;

    protected Mapper(Class clazz, CommandProvider<COMMAND_TYPE> defaultCommandProvider) {
        this.defaultCommandProvider = defaultCommandProvider;
    }

    public final Map<Method, COMMAND_TYPE> get() {
        return mappings;
    }

    public COMMAND_TYPE get(Method method) {
        return mappings.get(method);
    }

    public void createMapping(Class<?> clazz) {
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

    protected COMMAND_TYPE getDefaultCommand(Method method){
        return defaultCommandProvider.getCommand(method, this);
    }

}
