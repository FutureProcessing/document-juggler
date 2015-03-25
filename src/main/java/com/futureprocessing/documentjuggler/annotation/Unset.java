package com.futureprocessing.documentjuggler.annotation;

import com.futureprocessing.documentjuggler.annotation.internal.UpdateCommandProvider;
import com.futureprocessing.documentjuggler.update.command.UnsetCommand;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({METHOD, ANNOTATION_TYPE})
@UpdateCommandProvider(UnsetCommand.Provider.class)
public @interface Unset {
}
