package com.futureprocessing.documentjuggler;


import com.futureprocessing.documentjuggler.insert.InsertConsumer;
import com.futureprocessing.documentjuggler.insert.InsertProcessor;
import com.futureprocessing.documentjuggler.query.QueriedDocuments;
import com.futureprocessing.documentjuggler.query.QueriedDocumentsImpl;
import com.futureprocessing.documentjuggler.query.QueryConsumer;
import com.futureprocessing.documentjuggler.query.QueryProcessor;
import com.futureprocessing.documentjuggler.read.ReadProcessor;
import com.futureprocessing.documentjuggler.update.UpdateProcessor;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class BaseRepository<MODEL> implements Repository<MODEL> {

    private final DBCollection dbCollection;

    private ReadProcessor<MODEL> readProcessor;
    private QueryProcessor<MODEL> queryProcessor;
    private InsertProcessor<MODEL> insertProcessor;
    private UpdateProcessor<MODEL> updateProcessor;

    public BaseRepository(DBCollection dbCollection, Class<MODEL> modelClass) {
        this.dbCollection = dbCollection;

        readProcessor = new ReadProcessor<>(modelClass, dbCollection);
        queryProcessor = new QueryProcessor<>(modelClass);
        insertProcessor = new InsertProcessor<>(modelClass);
        updateProcessor = new UpdateProcessor<>(modelClass);
    }

    @Override
    public QueriedDocuments<MODEL> find(QueryConsumer<MODEL> consumer) {
        return new QueriedDocumentsImpl<>(dbCollection, queryProcessor.process(consumer), readProcessor, updateProcessor);
    }

    @Override
    public QueriedDocuments<MODEL> find() {
        return new QueriedDocumentsImpl<>(dbCollection, null, readProcessor, updateProcessor);
    }

    @Override
    public String insert(InsertConsumer<MODEL> consumer) {
        BasicDBObject document = insertProcessor.process(consumer);

        dbCollection.insert(document);
        return document.getObjectId("_id").toHexString();
    }

}