package com.futureprocessing.documentjuggler.query.expression;

import com.futureprocessing.documentjuggler.query.QueryProcessor;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.function.Consumer;


public final class NorQueryExpression<MODEL> extends QueryExpression<MODEL> {

    protected NorQueryExpression(Consumer<MODEL> consumer1, Consumer<MODEL> consumer2) {
        super(consumer1, consumer2);
    }

    protected NorQueryExpression(Consumer<MODEL> consumer, QueryExpression<MODEL> expression) {
        super(consumer, expression);
    }

    protected NorQueryExpression(QueryExpression<MODEL> expression, Consumer<MODEL> consumer) {
        super(expression, consumer);
    }

    protected NorQueryExpression(QueryExpression<MODEL> expression1, QueryExpression<MODEL> expression2) {
        super(expression1, expression2);
    }

    @Override
    public DBObject evaluate(QueryProcessor<MODEL> processor) {
        return new BasicDBObject("$nor", (evaluateComponents(processor)));
    }
}

