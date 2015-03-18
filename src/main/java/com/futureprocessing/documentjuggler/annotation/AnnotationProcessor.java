package com.futureprocessing.documentjuggler.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.reflect.AnnotatedElement;
import java.util.HashSet;
import java.util.Set;

public final class AnnotationProcessor<A extends Annotation> {

    private final Class<A> annotationClass;

    public static <A extends  Annotation> AnnotationProcessor<A> annotation(Class<A> annotationClass) {
        return new AnnotationProcessor<>(annotationClass);
    }

    private AnnotationProcessor(Class<A> annotationClass) {
        this.annotationClass = annotationClass;
    }

    private A getFrom(AnnotatedElement element, Set<Annotation> checked) {
        for (Annotation annotation : element.getAnnotations()) {
            if(checked.contains(annotation)) {
                continue;
            }

            if(annotation.annotationType() == annotationClass) {
                return (A) annotation;
            }

            checked.add(annotation);
            A result = getFrom(annotation.annotationType(), checked);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public A getFrom(AnnotatedElement element) {
        return getFrom(element, new HashSet<>());
    }

    public boolean isPresent(AnnotatedElement element) {
        return getFrom(element) != null;
    }
}
