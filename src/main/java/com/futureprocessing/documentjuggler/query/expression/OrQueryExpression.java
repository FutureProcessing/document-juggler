package com.futureprocessing.documentjuggler.query.expression;

import com.futureprocessing.documentjuggler.query.QueryConsumer;
import com.futureprocessing.documentjuggler.query.QueryProcessor;
import com.mongodb.DBObject;

import java.util.List;
import java.util.Optional;

import static com.mongodb.QueryBuilder.start;


public class OrQueryExpression<MODEL> extends QueryExpression<MODEL> {

    private final List<QueryConsumer<MODEL>> consumers;

    public OrQueryExpression(List<QueryConsumer<MODEL>> consumers) {
        this.consumers = consumers;
    }

    @Override
    public DBObject evaluate(QueryProcessor<MODEL> processor) {
        return start().or(consumers.stream()
                .map(consumers -> processor.process(Optional.of(consumers)))
                .toArray(size -> new DBObject[size])).get();
    }
}

