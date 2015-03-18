package com.futureprocessing.documentjuggler.annotation;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@DbField("_id")
@ObjectId
@Retention(RUNTIME)
@Target({METHOD, ANNOTATION_TYPE})
public @interface Id {
}
