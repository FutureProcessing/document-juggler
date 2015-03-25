package com.futureprocessing.documentjuggler.commons;


import com.futureprocessing.documentjuggler.Context;
import com.futureprocessing.documentjuggler.annotation.Forbidden;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static com.futureprocessing.documentjuggler.annotation.AnnotationReader.from;
import static com.futureprocessing.documentjuggler.commons.Validator.validateField;
import static com.futureprocessing.documentjuggler.commons.Validator.validateInterface;

public abstract class Mapper<COMMAND_TYPE> {

    private final Set<Class> mappedClasses = new HashSet<>();
    private final Map<Method, COMMAND_TYPE> mappings = new HashMap<>();
    private final Context context;
    private final CommandProvider<COMMAND_TYPE> defaultCommandProvider;
    private final CommandProvider<COMMAND_TYPE> forbiddenCommandProvider;

    protected Mapper(Context context, CommandProvider<COMMAND_TYPE> defaultCommandProvider, CommandProvider<COMMAND_TYPE> forbiddenCommandProvider) {
        this.context = context;
        this.defaultCommandProvider = defaultCommandProvider;
        this.forbiddenCommandProvider = forbiddenCommandProvider;
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

    private COMMAND_TYPE getCommand(Method method) {

        if (ForbiddenChecker.isForbidden(method, context) || isForbidden(method)) {
            return forbiddenCommandProvider.getCommand(method, this);
        }

        Optional<COMMAND_TYPE> command = getAnnotationBasedCommand(method);
        if (command.isPresent()) {
            return command.get();
        }

        return getDefaultCommand(method);
    }

    @SuppressWarnings("unchecked")
    private Optional<COMMAND_TYPE> getAnnotationBasedCommand(Method method) {

        Class contextClass = context.getContextAnnotationClass();
        Annotation readContext = from(method).read(contextClass);
        if (readContext != null) {

            try {
                Method commandProviderMethod = contextClass.getMethod("value");
                Class<? extends CommandProvider<COMMAND_TYPE>> commandClass = (Class<? extends CommandProvider<COMMAND_TYPE>>) commandProviderMethod.invoke(readContext);

                CommandProvider<COMMAND_TYPE> commandProvider = commandClass.newInstance();
                return Optional.of(commandProvider.getCommand(method, this));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return Optional.empty();
    }

    protected abstract boolean isForbidden(Method method);

    private COMMAND_TYPE getDefaultCommand(Method method) {
        return defaultCommandProvider.getCommand(method, this);
    }

}
