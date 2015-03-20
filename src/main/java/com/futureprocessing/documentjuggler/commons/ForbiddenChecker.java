package com.futureprocessing.documentjuggler.commons;

import com.futureprocessing.documentjuggler.Context;
import com.futureprocessing.documentjuggler.annotation.Forbidden;

import java.lang.reflect.Method;
import java.util.List;

import static com.futureprocessing.documentjuggler.annotation.AnnotationReader.from;
import static java.util.Arrays.binarySearch;

public final class ForbiddenChecker {

    public static boolean isForbidden(Method method, Context context) {
        List<Forbidden> forbidden = from(method).readAll(Forbidden.class);

        if (forbidden.isEmpty()) {
            return false;
        }

        for (Forbidden f : forbidden) {
            if (binarySearch(f.value(), context) >= 0) {
                return true;
            }
        }

        return false;
    }

    private ForbiddenChecker() {
    }

    ;
}
