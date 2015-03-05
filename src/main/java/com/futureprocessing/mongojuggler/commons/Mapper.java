package com.futureprocessing.mongojuggler.commons;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.futureprocessing.mongojuggler.commons.Validator.validateInterface;

public abstract class Mapper<C> {

//    private final Map<Class, Map<Method, C>> mappings = new HashMap<>();
    private final Set<Class> mappedClasses = new HashSet<>();
    private final Map<Method, C> mappings = new HashMap<>();

    protected Mapper(Class clazz) {
        createMapping(clazz);
    }

    public final Map<Method, C> get(Class<?> clazz) {
        return mappings;
    }

    protected void createMapping(Class<?> clazz) {
        if (!mappedClasses.contains(clazz)) {
            validateInterface(clazz);

//            Map<Method, C> map = new HashMap<>();
//            mappings.put(clazz, map);

            for (Method method : clazz.getMethods()) {
                mappings.put(method, getCommand(method));
            }

            mappedClasses.add(clazz);
        }
    }

    protected abstract C getCommand(Method method);

}
