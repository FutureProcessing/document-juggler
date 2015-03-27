package com.futureprocessing.documentjuggler.annotation;

import com.futureprocessing.documentjuggler.commons.EqualsProvider;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({TYPE, ANNOTATION_TYPE})
public @interface Equals {

    Class<? extends EqualsProvider> value();
}
