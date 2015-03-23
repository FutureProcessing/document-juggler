package com.futureprocessing.documentjuggler.query.expression;

import com.futureprocessing.documentjuggler.query.QueryConsumer;
import com.futureprocessing.documentjuggler.query.QueryProcessor;
import com.mongodb.DBObject;

import static com.mongodb.QueryBuilder.start;


public final class AndQueryExpression<MODEL> extends QueryExpression<MODEL> {

    protected AndQueryExpression(QueryConsumer<MODEL> consumer1, QueryConsumer<MODEL> consumer2) {
        super(consumer1, consumer2);
    }

    protected AndQueryExpression(QueryConsumer<MODEL> consumer, QueryExpression<MODEL> expression) {
        super(consumer, expression);
    }

    protected AndQueryExpression(QueryExpression<MODEL> expression, QueryConsumer<MODEL> consumer) {
        super(expression, consumer);
    }

    protected AndQueryExpression(QueryExpression<MODEL> expression1, QueryExpression<MODEL> expression2) {
        super(expression1, expression2);
    }

    @Override
    public DBObject evaluate(QueryProcessor<MODEL> processor) {
        return start().and(evaluateComponents(processor)).get();
    }
}

