package com.futureprocessing.documentjuggler.query;


import com.futureprocessing.documentjuggler.Operator;
import com.futureprocessing.documentjuggler.insert.InsertProcessResult;
import com.futureprocessing.documentjuggler.insert.InsertProxy;
import com.futureprocessing.documentjuggler.insert.InserterConsumer;
import com.futureprocessing.documentjuggler.insert.InserterMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class QueryProcessor<MODEL> {

    private final Operator<MODEL, QuerierMapper> operator;
    private final DBCollection collection;

    public QueryProcessor(DBCollection collection, Operator<MODEL, QuerierMapper> operator) {
        this.collection = collection;
        this.operator = operator;
    }

    public DBObject process(QuerierConsumer<MODEL> consumer) {
        MODEL querier = QueryProxy.create(operator.getRootClass(), operator.getMapper().get());
        consumer.accept(querier);

        return QueryProxy.extract(querier).toDBObject();
    }

}
