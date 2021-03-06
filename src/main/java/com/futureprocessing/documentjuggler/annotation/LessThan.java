package com.futureprocessing.documentjuggler.annotation;

import com.futureprocessing.documentjuggler.annotation.internal.QueryCommandProvider;
import com.futureprocessing.documentjuggler.query.command.LessThanQueryCommand;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.futureprocessing.documentjuggler.Context.*;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Forbidden({READ, INSERT, UPDATE})
@Retention(RUNTIME)
@Target({METHOD, ANNOTATION_TYPE})
@QueryCommandProvider(LessThanQueryCommand.Provider.class)
public @interface LessThan {
}
