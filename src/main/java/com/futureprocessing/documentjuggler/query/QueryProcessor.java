package com.futureprocessing.documentjuggler.query;


import com.mongodb.DBObject;

public class QueryProcessor<MODEL> {

    private final Class<MODEL> modelClass;
    private final QueryMapper mapper;

    public QueryProcessor(Class<MODEL> modelClass) {
        this.modelClass = modelClass;
        this.mapper = new QueryMapper(modelClass);
    }

    public DBObject process(QueryConsumer<MODEL> consumer) {
        MODEL querier = QueryProxy.create(modelClass, mapper.get());
        consumer.accept(querier);

        return QueryProxy.extract(querier).toDBObject();
    }

}
