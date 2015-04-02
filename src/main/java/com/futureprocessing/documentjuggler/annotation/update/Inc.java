package com.futureprocessing.documentjuggler.annotation.update;

import com.futureprocessing.documentjuggler.annotation.internal.UpdateCommandProvider;
import com.futureprocessing.documentjuggler.update.command.IncrementUpdateCommand;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({METHOD, ANNOTATION_TYPE})
@UpdateCommandProvider(IncrementUpdateCommand.Provider.class)
public @interface Inc {
}
