package com.futureprocessing.documentjuggler.query;


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class QueryExpression<MODEL> {

    private final List<QueryConsumer<MODEL>> consumers;

    public QueryExpression(List<QueryConsumer<MODEL>> consumers) {
        this.consumers = consumers;
    }

    public DBObject evaluate(QueryProcessor<MODEL> processor) {
        return new BasicDBObject("$or", consumers.stream().map(consumers -> processor.process(Optional.of(consumers))).collect(toList()));
    }
}
