package com.futureprocessing.documentjuggler.query;


import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.query.command.QueryCommand;
import com.futureprocessing.documentjuggler.query.expression.QueryExpression;
import com.mongodb.DBObject;

import java.util.function.Consumer;

public class QueryProcessor<MODEL> {

    private final Class<MODEL> modelClass;
    private final Mapper<QueryCommand> mapper;

    public QueryProcessor(Class<MODEL> modelClass) {
        this.modelClass = modelClass;
        this.mapper = QueryMapper.map(modelClass);
    }

    public QueryProcessor(Class<MODEL> modelClass, Mapper<QueryCommand> mapper) {
        this.modelClass = modelClass;
        this.mapper = mapper;
    }

    public DBObject process(Consumer<MODEL> consumer) {
        if (consumer == null) {
            return null;
        }

        MODEL querier = QueryProxy.create(modelClass, mapper);
        consumer.accept(querier);

        return QueryProxy.extract(querier).toDBObject();
    }

    public DBObject process(QueryExpression<MODEL> expression) {
        return expression.evaluate(this);
    }

}
