package com.futureprocessing.documentjuggler.annotation;

import com.futureprocessing.documentjuggler.Context;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Forbidden {

    Context[] value();
}
