package com.futureprocessing.documentjuggler.annotation;

import com.futureprocessing.documentjuggler.annotation.internal.InsertCommandProvider;
import com.futureprocessing.documentjuggler.annotation.internal.ReadCommandProvider;
import com.futureprocessing.documentjuggler.annotation.internal.UpdateCommandProvider;
import com.futureprocessing.documentjuggler.insert.EmbeddedInsertCommandProvider;
import com.futureprocessing.documentjuggler.read.EmbeddedReadCommandProvider;
import com.futureprocessing.documentjuggler.update.command.providers.EmbeddedUpdateCommandProvider;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({METHOD, ANNOTATION_TYPE})
@ReadCommandProvider(EmbeddedReadCommandProvider.class)
@InsertCommandProvider(EmbeddedInsertCommandProvider.class)
@UpdateCommandProvider(EmbeddedUpdateCommandProvider.class)
public @interface DbEmbeddedDocument {

}
