package com.futureprocessing.documentjuggler.commons;

import com.futureprocessing.documentjuggler.annotation.Equals;

public interface EqualsProvider<MODEL> {

    boolean areEqual(MODEL model, Object obj);

    class Creator {

        public static EqualsProvider fromAnnotation(Class readerType) {
            Equals equalsAnnotation = (Equals) readerType.getAnnotation(Equals.class);
            if (equalsAnnotation != null) {
                Class<? extends EqualsProvider> value = equalsAnnotation.value();
                try {
                    return value.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Problem when instatiating equals provider", e);
                }
            }

            return null;
        }
    }

    class BaseEquals {
        public static boolean isEqualClass(Object model, Object obj) {
            return obj != null && model.getClass() == obj.getClass();
        }
    }
}
