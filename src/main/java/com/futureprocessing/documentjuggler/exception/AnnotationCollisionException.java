package com.futureprocessing.documentjuggler.exception;


import java.lang.annotation.Annotation;

public class AnnotationCollisionException extends RuntimeException {

    private final Class<? extends Annotation> annotationClass;

    public AnnotationCollisionException(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }
}
