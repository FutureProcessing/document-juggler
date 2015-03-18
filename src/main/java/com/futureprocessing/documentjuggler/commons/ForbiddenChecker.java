package com.futureprocessing.documentjuggler.commons;

import com.futureprocessing.documentjuggler.Context;
import com.futureprocessing.documentjuggler.annotation.Forbidden;

import java.lang.reflect.Method;

import static java.util.Arrays.binarySearch;

public final class ForbiddenChecker {

    public static boolean isForbidden(Method method, Context context) {
        Forbidden forbidden = method.getAnnotation(Forbidden.class);

        if (forbidden == null) {
            return false;
        }

        return binarySearch(forbidden.value(), context) >= 0;
    }

    private ForbiddenChecker() {
    }

    ;
}