package com.futureprocessing.documentjuggler.query.expression;


import com.futureprocessing.documentjuggler.query.QueryConsumer;
import com.futureprocessing.documentjuggler.query.QueryProcessor;
import com.mongodb.DBObject;

import static java.util.Arrays.asList;

public class QueryExpression<MODEL> {

    public static <MODEL> OrQueryExpression<MODEL> or(QueryConsumer<MODEL> consumer1, QueryConsumer<MODEL> consumer2) {
        return new OrQueryExpression<>(asList(consumer1, consumer2));
    }

    protected QueryExpression() {}

    public DBObject evaluate(QueryProcessor<MODEL> processor) {
        throw new UnsupportedOperationException("Hack: Lambda not work in repository with overloaded interface");
    }
}
