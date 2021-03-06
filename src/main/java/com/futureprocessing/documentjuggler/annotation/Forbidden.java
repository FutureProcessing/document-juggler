package com.futureprocessing.documentjuggler.annotation;

import com.futureprocessing.documentjuggler.Context;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({METHOD, ANNOTATION_TYPE})
public @interface Forbidden {

    Context[] value();
}
