package com.futureprocessing.documentjuggler.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashSet;
import java.util.Set;

public final class AnnotationProcessor {

    private final AnnotatedElement element;

    public static AnnotationProcessor process(AnnotatedElement method) {
        return new AnnotationProcessor(method);
    }

    private AnnotationProcessor(AnnotatedElement element) {
        this.element = element;
    }

    private <A extends Annotation> A get(Class<A> annotationClass, AnnotatedElement element, Set<Annotation> checked) {
        for (Annotation annotation : element.getAnnotations()) {
            if (checked.contains(annotation)) {
                continue;
            }

            if (annotation.annotationType() == annotationClass) {
                return (A) annotation;
            }

            checked.add(annotation);
            A result = get(annotationClass, annotation.annotationType(), checked);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public <A extends Annotation> A get(Class<A> annotationClass) {
        return get(annotationClass, element, new HashSet<>());
    }

    public <A extends Annotation> boolean has(Class<A> annotationClass) {
        return get(annotationClass) != null;
    }
}
