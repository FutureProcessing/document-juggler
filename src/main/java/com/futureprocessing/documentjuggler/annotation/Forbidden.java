package com.futureprocessing.documentjuggler.annotation;

import com.futureprocessing.documentjuggler.Context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface Forbidden {

    Context[] value();
}
