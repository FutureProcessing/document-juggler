package com.futureprocessing.documentjuggler.annotation.internal;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;

@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, ANNOTATION_TYPE})
public @interface MightBeNegated {
}
