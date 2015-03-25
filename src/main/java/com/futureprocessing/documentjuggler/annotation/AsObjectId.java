package com.futureprocessing.documentjuggler.annotation;

import com.futureprocessing.documentjuggler.annotation.internal.InsertCommandProvider;
import com.futureprocessing.documentjuggler.annotation.internal.QueryCommandProvider;
import com.futureprocessing.documentjuggler.annotation.internal.ReadCommandProvider;
import com.futureprocessing.documentjuggler.insert.command.IdInsertCommand;
import com.futureprocessing.documentjuggler.query.command.IdQueryCommand;
import com.futureprocessing.documentjuggler.read.command.IdReadCommand;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Retention(RUNTIME)
@Target({METHOD, ANNOTATION_TYPE})
@ReadCommandProvider(IdReadCommand.Provider.class)
@QueryCommandProvider(IdQueryCommand.Provider.class)
@InsertCommandProvider(IdInsertCommand.Provider.class)
public @interface AsObjectId {

}
