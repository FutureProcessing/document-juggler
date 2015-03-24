package com.futureprocessing.documentjuggler.commons;


import com.futureprocessing.documentjuggler.Context;

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
    private final Context context;
    private final CommandProvider<COMMAND_TYPE> defaultCommandProvider;

    protected Mapper(Context context, CommandProvider<COMMAND_TYPE> defaultCommandProvider) {
        this.context = context;
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
                mappings.put(method, getCommand2(method));
            }

            mappedClasses.add(clazz);
        }
    }

    private COMMAND_TYPE getCommand2(Method method) {

        COMMAND_TYPE command = getForbidden(method);
        if (command !=null){
            return command;
        }

        command = ContextCommandsMapper.getCommand(method, this, context.getContextAnnotationClass());
        if (command != null){
            return command;
        }

        return getDefaultCommand(method);
    }

    protected abstract COMMAND_TYPE getForbidden(Method method);

    protected COMMAND_TYPE getDefaultCommand(Method method){
        return defaultCommandProvider.getCommand(method, this);
    }

}
