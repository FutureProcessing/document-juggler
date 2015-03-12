package com.futureprocessing.documentjuggler;


import com.futureprocessing.documentjuggler.insert.InsertConsumer;
import com.futureprocessing.documentjuggler.insert.InsertProcessor;
import com.futureprocessing.documentjuggler.query.*;
import com.futureprocessing.documentjuggler.read.ReaderMapper;
import com.futureprocessing.documentjuggler.update.UpdateProcessor;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class BaseRepository<MODEL> implements Repository<MODEL> {

    private final Operator<MODEL, QueryMapper> querierOperator;
    private final Operator<MODEL, ReaderMapper> readerOperator;

    private final DBCollection dbCollection;

    private QueryProcessor<MODEL> queryProcessor;
    private InsertProcessor<MODEL> insertProcessor;
    private UpdateProcessor<MODEL> updateProcessor;

    public BaseRepository(DBCollection dbCollection, Class<MODEL> modelClass) {
        this.dbCollection = dbCollection;

        this.querierOperator = new Operator<>(modelClass, new QueryMapper(modelClass));
        this.readerOperator = new Operator<>(modelClass, new ReaderMapper(modelClass));

        queryProcessor = new QueryProcessor<>(dbCollection, querierOperator);
        insertProcessor = new InsertProcessor<>(modelClass);
        updateProcessor = new UpdateProcessor<>(modelClass);
    }

    @Override
    public QueriedDocuments<MODEL> find(QueryConsumer<MODEL> queryConsumer) {
        return new QueriedDocumentsImpl<>(readerOperator, dbCollection,
                queryProcessor.process(queryConsumer), updateProcessor);
    }

    @Override
    public QueriedDocuments<MODEL> find() {
        return new QueriedDocumentsImpl<>(readerOperator, dbCollection, null, updateProcessor);
    }

    @Override
    public String insert(InsertConsumer<MODEL> consumer) {
        BasicDBObject document = insertProcessor.process(consumer);

        dbCollection.insert(document);
        return document.getObjectId("_id").toHexString();
    }

}
