package com.futureprocessing.documentjuggler.annotation;

import com.futureprocessing.documentjuggler.exception.AnnotationCollisionException;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

public final class AnnotationReader {

    private final AnnotatedElement element;

    public static AnnotationReader from(AnnotatedElement method) {
        return new AnnotationReader(method);
    }

    private AnnotationReader(AnnotatedElement element) {
        this.element = element;
    }

    private <A extends Annotation> List<A> readAll(Class<A> annotationClass, Collection<Annotation> annotations, Set<Annotation> checked) {
        Collection<Annotation> nextLevelAnnotations = new LinkedList<>();
        List<A> found = new LinkedList<>();

        if (annotations.isEmpty()) {
            return new LinkedList<>();
        }

        for (Annotation annotation : annotations) {
            if (checked.contains(annotation)) {
                continue;
            }

            if (annotation.annotationType() == annotationClass) {
                found.add((A) annotation);
            }

            checked.add(annotation);
            nextLevelAnnotations.addAll(asList(annotation.annotationType().getAnnotations()));
        }

        found.addAll(readAll(annotationClass, nextLevelAnnotations, checked));
        return found;
    }

    public <A extends Annotation> A read(Class<A> annotationClass) {
        Collection<Annotation> nextLevelAnnotations = new LinkedList<>();
        Set<Annotation> checked = new HashSet<>();

        for (Annotation annotation : element.getAnnotations()) {
            if (annotation.annotationType() == annotationClass) {
                return (A) annotation;
            }
            checked.add(annotation);
            nextLevelAnnotations.addAll(asList(annotation.annotationType().getAnnotations()));
        }

        List<A> result = readAll(annotationClass, nextLevelAnnotations, checked);

        if (result.size() > 1) {
            throw new AnnotationCollisionException(annotationClass);
        }

        return result.isEmpty() ? null : result.get(0);
    }

    public <A extends Annotation> List<A> readAll(Class<A> annotationClass) {
        Collection<Annotation> nextLevelAnnotations = new LinkedList<>();
        Set<Annotation> checked = new HashSet<>();
        List<A> result = new LinkedList<>();

        for (Annotation annotation : element.getAnnotations()) {
            if (annotation.annotationType() == annotationClass) {
                result.add((A) annotation);
            }
            checked.add(annotation);
            nextLevelAnnotations.addAll(asList(annotation.annotationType().getAnnotations()));
        }

        result.addAll(readAll(annotationClass, nextLevelAnnotations, checked));

        return unmodifiableList(result);
    }

    public <A extends Annotation> boolean isPresent(Class<A> annotationClass) {
        return read(annotationClass) != null;
    }
}
