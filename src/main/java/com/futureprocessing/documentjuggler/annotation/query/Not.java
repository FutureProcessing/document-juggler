package com.futureprocessing.documentjuggler.annotation.query;


import com.futureprocessing.documentjuggler.annotation.Forbidden;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.futureprocessing.documentjuggler.Context.*;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({METHOD, ANNOTATION_TYPE})
@Forbidden({READ, INSERT, UPDATE})
public @interface Not {

}
