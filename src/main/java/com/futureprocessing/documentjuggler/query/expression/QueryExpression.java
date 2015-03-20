package com.futureprocessing.documentjuggler.query.expression;


import com.futureprocessing.documentjuggler.query.QueryConsumer;
import com.futureprocessing.documentjuggler.query.QueryProcessor;
import com.mongodb.DBObject;

import java.util.Optional;

public class QueryExpression<MODEL> {

    private final QueryConsumer<MODEL> consumer1;
    private final QueryConsumer<MODEL> consumer2;

    private final QueryExpression<MODEL> expression1;
    private final QueryExpression<MODEL> expression2;

    public static <MODEL> OrQueryExpression<MODEL> or(QueryConsumer<MODEL> consumer1, QueryConsumer<MODEL> consumer2) {
        return new OrQueryExpression<>(consumer1, consumer2);
    }

    public static <MODEL> OrQueryExpression<MODEL> or(QueryExpression<MODEL> expression, QueryConsumer<MODEL> consumer) {
        return new OrQueryExpression<>(expression, consumer);
    }

    public static <MODEL> OrQueryExpression<MODEL> or(QueryConsumer<MODEL> consumer, QueryExpression<MODEL> expression) {
        return new OrQueryExpression<>(consumer, expression);
    }

    public static <MODEL> OrQueryExpression<MODEL> or(QueryExpression<MODEL> expression1, QueryExpression<MODEL> expression2) {
        return new OrQueryExpression<>(expression1, expression2);
    }

    public static <MODEL> AndQueryExpression<MODEL> and(QueryConsumer<MODEL> consumer1, QueryConsumer<MODEL> consumer2) {
        return new AndQueryExpression<>(consumer1, consumer2);
    }

    public static <MODEL> AndQueryExpression<MODEL> and(QueryExpression<MODEL> expression, QueryConsumer<MODEL> consumer) {
        return new AndQueryExpression<>(expression, consumer);
    }

    public static <MODEL> AndQueryExpression<MODEL> and(QueryConsumer<MODEL> consumer, QueryExpression<MODEL> expression) {
        return new AndQueryExpression<>(consumer, expression);
    }

    public static <MODEL> AndQueryExpression<MODEL> and(QueryExpression<MODEL> expression1, QueryExpression<MODEL> expression2) {
        return new AndQueryExpression<>(expression1, expression2);
    }

    protected QueryExpression(QueryConsumer<MODEL> consumer1, QueryConsumer<MODEL> consumer2) {
        this.consumer1 = consumer1;
        this.consumer2 = consumer2;

        this.expression1 = null;
        this.expression2 = null;
    }

    protected QueryExpression(QueryConsumer<MODEL> consumer, QueryExpression<MODEL> expression) {
        this.consumer1 = consumer;
        this.consumer2 = null;

        this.expression1 = null;
        this.expression2 = expression;
    }

    protected QueryExpression(QueryExpression<MODEL> expression, QueryConsumer<MODEL> consumer) {
        this.consumer1 = null;
        this.consumer2 = consumer;

        this.expression1 = expression;
        this.expression2 = null;
    }

    protected QueryExpression(QueryExpression<MODEL> expression1, QueryExpression<MODEL> expression2) {
        this.consumer1 = null;
        this.consumer2 = null;

        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    private DBObject toDbObject(QueryConsumer<MODEL> consumer, QueryExpression<MODEL> expression, QueryProcessor<MODEL> processor) {
        return expression == null ? processor.process(Optional.of(consumer)) : expression.evaluate(processor);
    }

    protected DBObject[] evaluateComponents(QueryProcessor<MODEL> processor) {
        DBObject[] objects = new DBObject[2];

        objects[0] = toDbObject(consumer1, expression1, processor);
        objects[1] = toDbObject(consumer2, expression2, processor);

        return objects;
    }

    public DBObject evaluate(QueryProcessor<MODEL> processor) {
        throw new UnsupportedOperationException("Hack: Lambda not work in repository with overloaded interface");
    }
}
