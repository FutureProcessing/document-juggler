package com.futureprocessing.documentjuggler.helper;


import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public final class Sets {

    public static <T> Set<T> asSet(T... objects) {
        return new HashSet<>(asList(objects));
    }

    private Sets() {
    }
}
