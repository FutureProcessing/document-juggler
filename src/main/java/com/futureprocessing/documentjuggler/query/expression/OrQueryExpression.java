package com.futureprocessing.documentjuggler.query.expression;

import com.futureprocessing.documentjuggler.query.QueryConsumer;
import com.futureprocessing.documentjuggler.query.QueryProcessor;
import com.mongodb.DBObject;

import static com.mongodb.QueryBuilder.start;


public final class OrQueryExpression<MODEL> extends QueryExpression<MODEL> {

    protected OrQueryExpression(QueryConsumer<MODEL> consumer1, QueryConsumer<MODEL> consumer2) {
        super(consumer1, consumer2);
    }

    protected OrQueryExpression(QueryConsumer<MODEL> consumer, QueryExpression<MODEL> expression) {
        super(consumer, expression);
    }

    protected OrQueryExpression(QueryExpression<MODEL> expression, QueryConsumer<MODEL> consumer) {
        super(expression, consumer);
    }

    protected OrQueryExpression(QueryExpression<MODEL> expression1, QueryExpression<MODEL> expression2) {
        super(expression1, expression2);
    }

    @Override
    public DBObject evaluate(QueryProcessor<MODEL> processor) {
        return start().or(evaluateComponents(processor)).get();
    }
}

