package com.futureprocessing.documentjuggler.query;


import com.futureprocessing.documentjuggler.query.expression.QueryExpression;
import com.mongodb.DBObject;

public class QueryProcessor<MODEL> {

    private final Class<MODEL> modelClass;
    private final QueryMapper mapper;

    public QueryProcessor(Class<MODEL> modelClass) {
        this.modelClass = modelClass;
        this.mapper = new QueryMapper(modelClass);
    }

    public DBObject process(QueryConsumer<MODEL> consumer) {
        if (consumer == null) {
            return null;
        }

        MODEL querier = QueryProxy.create(modelClass, mapper.get());
        consumer.accept(querier);

        return QueryProxy.extract(querier).toDBObject();
    }

    public DBObject process(QueryExpression<MODEL> expression) {
        return expression.evaluate(this);
    }

}
