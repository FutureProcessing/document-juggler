package com.futureprocessing.mongojuggler.commons;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.futureprocessing.mongojuggler.commons.Validator.validateInterface;

public abstract class Mapper<C> {

    private final Map<Class, Map<Method, C>> mappings = new HashMap<>();

    protected Mapper(Class clazz) {
        createMapping(clazz);
    }

    public final Map<Method, C> get(Class<?> clazz) {
        return mappings.get(clazz);
    }

    protected void createMapping(Class<?> clazz) {
        if (!mappings.containsKey(clazz)) {
            validateInterface(clazz);

            Map<Method, C> map = new HashMap<>();
            mappings.put(clazz, map);

            for (Method method : clazz.getMethods()) {
                map.put(method, getCommand(method));
            }
        }
    }

    protected abstract C getCommand(Method method);

}
