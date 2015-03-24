package com.futureprocessing.documentjuggler.annotation;

import com.futureprocessing.documentjuggler.annotation.internal.InsertContext;
import com.futureprocessing.documentjuggler.annotation.internal.ReadContext;
import com.futureprocessing.documentjuggler.insert.EmbeddedInsertCommandProvider;
import com.futureprocessing.documentjuggler.read.EmbeddedReadCommandProvider;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({METHOD, ANNOTATION_TYPE})
@ReadContext(
        commandProvider = EmbeddedReadCommandProvider.class
)
@InsertContext(
        commandProvider = EmbeddedInsertCommandProvider.class
)
public @interface DbEmbeddedDocument {

}
