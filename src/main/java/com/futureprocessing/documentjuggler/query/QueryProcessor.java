package com.futureprocessing.documentjuggler.query;


import com.futureprocessing.documentjuggler.Operator;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class QueryProcessor<MODEL> {

    private final Operator<MODEL, QueryMapper> operator;
    private final DBCollection collection;

    public QueryProcessor(DBCollection collection, Operator<MODEL, QueryMapper> operator) {
        this.collection = collection;
        this.operator = operator;
    }

    public DBObject process(QueryConsumer<MODEL> consumer) {
        MODEL querier = QueryProxy.create(operator.getRootClass(), operator.getMapper().get());
        consumer.accept(querier);

        return QueryProxy.extract(querier).toDBObject();
    }

}
