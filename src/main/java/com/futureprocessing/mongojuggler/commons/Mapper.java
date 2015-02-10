package com.futureprocessing.mongojuggler.commons;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class Mapper<C> {

    private final Map<Class, Map<Method, C>> mappings = new HashMap<>();

    public final Map<Method, C> get(Class<?> clazz) {
        Map<Method, C> map = mappings.get(clazz);
        if (map == null) {
            map = create(clazz);
            mappings.put(clazz, map);
        }
        return map;
    }

    private Map<Method, C> create(Class<?> clazz) {
        Map<Method, C> map = new HashMap<>();

        for (Method method : clazz.getMethods()) {
            map.put(method, getCommand(method));
        }

        return map;
    }

    protected abstract C getCommand(Method method);

}
