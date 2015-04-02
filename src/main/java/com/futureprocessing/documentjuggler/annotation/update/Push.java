package com.futureprocessing.documentjuggler.annotation.update;

import com.futureprocessing.documentjuggler.annotation.Forbidden;
import com.futureprocessing.documentjuggler.annotation.internal.UpdateCommandProvider;
import com.futureprocessing.documentjuggler.update.command.providers.PushCommandProvider;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.futureprocessing.documentjuggler.Context.*;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({METHOD, ANNOTATION_TYPE})
@Forbidden({READ, INSERT, QUERY})
@UpdateCommandProvider(PushCommandProvider.class)
public @interface Push {
}
