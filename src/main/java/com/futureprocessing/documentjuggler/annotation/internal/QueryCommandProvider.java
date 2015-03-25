package com.futureprocessing.documentjuggler.annotation.internal;


import com.futureprocessing.documentjuggler.commons.CommandProvider;
import com.futureprocessing.documentjuggler.query.command.QueryCommand;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
public @interface QueryCommandProvider {

    Class<? extends CommandProvider<QueryCommand>> value();
}
