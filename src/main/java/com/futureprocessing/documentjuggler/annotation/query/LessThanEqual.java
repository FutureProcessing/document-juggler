package com.futureprocessing.documentjuggler.annotation.query;

import com.futureprocessing.documentjuggler.annotation.internal.QueryCommandProvider;
import com.futureprocessing.documentjuggler.annotation.internal.ComparisonOperator;
import com.futureprocessing.documentjuggler.query.command.LessThanEqualQueryCommand;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@ComparisonOperator
@Retention(RUNTIME)
@Target({METHOD, ANNOTATION_TYPE})
@QueryCommandProvider(LessThanEqualQueryCommand.Provider.class)
public @interface LessThanEqual {
}
