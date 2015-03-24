package com.futureprocessing.documentjuggler.annotation.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ReturnType {

    Class value();
}
