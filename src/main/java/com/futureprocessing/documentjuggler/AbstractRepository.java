package com.futureprocessing.documentjuggler;

import com.futureprocessing.documentjuggler.insert.InsertProcessor;
import com.futureprocessing.documentjuggler.query.QueriedDocuments;
import com.futureprocessing.documentjuggler.query.QueriedDocumentsImpl;
import com.futureprocessing.documentjuggler.query.QueryProcessor;
import com.futureprocessing.documentjuggler.query.expression.QueryExpression;
import com.futureprocessing.documentjuggler.read.ReadProcessor;
import com.futureprocessing.documentjuggler.update.UpdateProcessor;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;

import java.util.function.Consumer;

public abstract class AbstractRepository<MODEL, QUERIER> {

    private final DBCollection dbCollection;

    private ReadProcessor<MODEL> readProcessor;
    private QueryProcessor<QUERIER> queryProcessor;
    private InsertProcessor<MODEL> insertProcessor;
    private UpdateProcessor<MODEL> updateProcessor;

    public AbstractRepository(DBCollection dbCollection, Class<MODEL> modelClass, Class<QUERIER> querierClass) {
        this.dbCollection = dbCollection;

        this.readProcessor = new ReadProcessor<>(modelClass, dbCollection);
        this.queryProcessor = new QueryProcessor<>(querierClass);
        this.insertProcessor = new InsertProcessor<>(modelClass);
        this.updateProcessor = new UpdateProcessor<>(modelClass);
    }

    public QueriedDocuments<MODEL> find(Consumer<QUERIER> consumer) {
        return new QueriedDocumentsImpl<>(dbCollection, queryProcessor.process(consumer), readProcessor, updateProcessor);
    }

    public QueriedDocuments<MODEL> find(QueryExpression<QUERIER> expression) {
        return new QueriedDocumentsImpl<>(dbCollection, queryProcessor.process(expression), readProcessor, updateProcessor);
    }

    public QueriedDocuments<MODEL> find() {
        return find((Consumer<QUERIER>) null);
    }

    public QueriedDocuments<MODEL> find(DBObject query) {
        return new QueriedDocumentsImpl<>(dbCollection, query, readProcessor, updateProcessor);
    }

    public String insert(Consumer<MODEL> consumer) {
        BasicDBObject document = insertProcessor.process(consumer);


        dbCollection.insert(document);
        if (document.get("_id") instanceof ObjectId) {
            return document.getObjectId("_id").toHexString();
        }
        return document.get("_id").toString();
    }

}
