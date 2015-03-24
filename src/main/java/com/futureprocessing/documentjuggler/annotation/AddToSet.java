package com.futureprocessing.documentjuggler.annotation;

import com.futureprocessing.documentjuggler.annotation.internal.UpdateContext;
import com.futureprocessing.documentjuggler.update.command.providers.AddToSetCommandProvider;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.futureprocessing.documentjuggler.Context.*;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({METHOD, ANNOTATION_TYPE})
@Forbidden({READ, INSERT, QUERY})
@UpdateContext(
        commandProvider = AddToSetCommandProvider.class
)
public @interface AddToSet {
}
