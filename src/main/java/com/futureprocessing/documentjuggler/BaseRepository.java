package com.futureprocessing.documentjuggler;


import com.futureprocessing.documentjuggler.insert.InsertProcessor;
import com.futureprocessing.documentjuggler.insert.InserterConsumer;
import com.futureprocessing.documentjuggler.insert.InserterMapper;
import com.futureprocessing.documentjuggler.query.*;
import com.futureprocessing.documentjuggler.read.ReaderMapper;
import com.futureprocessing.documentjuggler.update.UpdateProcessor;
import com.futureprocessing.documentjuggler.update.UpdaterMapper;
import com.mongodb.DBCollection;

public class BaseRepository<MODEL> implements Repository<MODEL> {

    private final Operator<MODEL, InserterMapper> inserterOperator;
    private final Operator<MODEL, QuerierMapper> querierOperator;
    private final Operator<MODEL, ReaderMapper> readerOperator;
    private final Operator<MODEL, UpdaterMapper> updaterOperator;

    private final DBCollection dbCollection;

    private QueryProcessor<MODEL> queryProcessor;
    private InsertProcessor<MODEL> insertProcessor;
    private UpdateProcessor<MODEL> updateProcessor;

    public BaseRepository(DBCollection dbCollection, Class<MODEL> modelClass) {
        this.dbCollection = dbCollection;

        this.inserterOperator = new Operator<>(modelClass, new InserterMapper(modelClass));
        this.querierOperator = new Operator<>(modelClass, new QuerierMapper(modelClass));
        this.readerOperator = new Operator<>(modelClass, new ReaderMapper(modelClass));
        this.updaterOperator = new Operator<>(modelClass, new UpdaterMapper(modelClass));

        queryProcessor = new QueryProcessor<>(dbCollection, querierOperator);
        insertProcessor = new InsertProcessor<>(dbCollection, inserterOperator);
        updateProcessor = new UpdateProcessor<>(dbCollection, updaterOperator);
    }

    @Override
    public QueriedDocuments<MODEL> find(QuerierConsumer<MODEL> querierConsumer) {
        return new QueriedDocumentsImpl<>(readerOperator, dbCollection,
                queryProcessor.process(querierConsumer), updateProcessor);
    }

    @Override
    public QueriedDocuments<MODEL> find() {
        return new QueriedDocumentsImpl<>(readerOperator, dbCollection, null, updateProcessor);
    }

    @Override
    public String insert(InserterConsumer<MODEL> consumer) {
        return insertProcessor.process(consumer).execute();
    }

}
