package com.futureprocessing.documentjuggler.query.expression;

import com.futureprocessing.documentjuggler.query.QueryProcessor;
import com.mongodb.DBObject;

import java.util.function.Consumer;

import static com.mongodb.QueryBuilder.start;


public final class AndQueryExpression<MODEL> extends QueryExpression<MODEL> {

    protected AndQueryExpression(Consumer<MODEL> consumer1, Consumer<MODEL> consumer2) {
        super(consumer1, consumer2);
    }

    protected AndQueryExpression(Consumer<MODEL> consumer, QueryExpression<MODEL> expression) {
        super(consumer, expression);
    }

    protected AndQueryExpression(QueryExpression<MODEL> expression, Consumer<MODEL> consumer) {
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

