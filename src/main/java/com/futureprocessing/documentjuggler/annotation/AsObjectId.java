package com.futureprocessing.documentjuggler.annotation;

import com.futureprocessing.documentjuggler.annotation.internal.InsertContext;
import com.futureprocessing.documentjuggler.annotation.internal.QueryContext;
import com.futureprocessing.documentjuggler.annotation.internal.ReadContext;
import com.futureprocessing.documentjuggler.annotation.internal.ReturnType;
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
@ReadContext(
        returns = @ReturnType(String.class),
        command = IdReadCommand.class
)
@QueryContext(
        command = IdQueryCommand.class
)
@InsertContext(
        command = IdInsertCommand.class
)
public @interface AsObjectId {

}
