package com.futureprocessing.documentjuggler.query.expression;


import com.futureprocessing.documentjuggler.query.QueryProcessor;
import com.mongodb.DBObject;

public interface QueryExpression<MODEL> {

    DBObject evaluate(QueryProcessor<MODEL> processor);
}
