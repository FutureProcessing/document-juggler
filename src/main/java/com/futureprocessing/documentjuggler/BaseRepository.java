package com.futureprocessing.documentjuggler;


import com.futureprocessing.documentjuggler.commons.CollectionExtractor;
import com.futureprocessing.documentjuggler.insert.InsertConsumer;
import com.futureprocessing.documentjuggler.insert.InsertProcessor;
import com.futureprocessing.documentjuggler.query.*;
import com.futureprocessing.documentjuggler.read.ReadProcessor;
import com.futureprocessing.documentjuggler.update.UpdateProcessor;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

import java.util.Optional;

public class BaseRepository<MODEL> implements Repository<MODEL> {

    private final DBCollection dbCollection;

    private ReadProcessor<MODEL> readProcessor;
    private QueryProcessor<MODEL> queryProcessor;
    private InsertProcessor<MODEL> insertProcessor;
    private UpdateProcessor<MODEL> updateProcessor;

    public BaseRepository(DB db, Class<MODEL> modelClass) {
        this(CollectionExtractor.getDBCollection(db, modelClass), modelClass);
    }

    public BaseRepository(DBCollection dbCollection, Class<MODEL> modelClass) {
        this.dbCollection = dbCollection;

        this.readProcessor = new ReadProcessor<>(modelClass, dbCollection);
        this.queryProcessor = new QueryProcessor<>(modelClass);
        this.insertProcessor = new InsertProcessor<>(modelClass);
        this.updateProcessor = new UpdateProcessor<>(modelClass);
    }

    @Override
    public QueriedDocuments<MODEL> find(QueryConsumer<MODEL> consumer) {
        return new QueriedDocumentsImpl<>(dbCollection, queryProcessor.process(Optional.ofNullable(consumer)), readProcessor, updateProcessor);
    }

    public QueriedDocuments<MODEL> find(QueryExpression<MODEL> expression) {
        return new QueriedDocumentsImpl<>(dbCollection, queryProcessor.process(expression), readProcessor, updateProcessor);
    }

    @Override
    public QueriedDocuments<MODEL> find() {
        return find((QueryConsumer<MODEL>)null);
    }

    @Override
    public String insert(InsertConsumer<MODEL> consumer) {
        BasicDBObject document = insertProcessor.process(consumer);

        dbCollection.insert(document);
        return document.getObjectId("_id").toHexString();
    }

}
