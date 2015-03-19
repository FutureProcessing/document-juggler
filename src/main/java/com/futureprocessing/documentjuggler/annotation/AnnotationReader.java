package com.futureprocessing.documentjuggler.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashSet;
import java.util.Set;

public final class AnnotationReader {

    private final AnnotatedElement element;

    public static AnnotationReader process(AnnotatedElement method) {
        return new AnnotationReader(method);
    }

    private AnnotationReader(AnnotatedElement element) {
        this.element = element;
    }

    private <A extends Annotation> A read(Class<A> annotationClass, AnnotatedElement element, Set<Annotation> checked) {
        for (Annotation annotation : element.getAnnotations()) {
            if (checked.contains(annotation)) {
                continue;
            }

            if (annotation.annotationType() == annotationClass) {
                return (A) annotation;
            }

            checked.add(annotation);
            A result = read(annotationClass, annotation.annotationType(), checked);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public <A extends Annotation> A read(Class<A> annotationClass) {
        return read(annotationClass, element, new HashSet<>());
    }

    public <A extends Annotation> boolean isPresent(Class<A> annotationClass) {
        return read(annotationClass) != null;
    }
}
