package com.futureprocessing.documentjuggler;


import com.futureprocessing.documentjuggler.insert.InsertProxy;
import com.futureprocessing.documentjuggler.insert.InserterConsumer;
import com.futureprocessing.documentjuggler.insert.InserterMapper;
import com.futureprocessing.documentjuggler.query.*;
import com.futureprocessing.documentjuggler.read.ReaderMapper;
import com.futureprocessing.documentjuggler.update.UpdaterMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class BaseRepository<MODEL> implements Repository<MODEL> {

    private final Operator<MODEL, InserterMapper> inserterOperator;
    private final Operator<MODEL, QuerierMapper> querierOperator;
    private final Operator<MODEL, ReaderMapper> readerOperator;
    private final Operator<MODEL, UpdaterMapper> updaterOperator;

    private final DBCollection dbCollection;

    private DBObjectTransformer preInsertTransformer;

    public BaseRepository(DBCollection dbCollection, Class<MODEL> modelClass) {
        this.dbCollection = dbCollection;

        this.inserterOperator = new Operator<>(modelClass, new InserterMapper(modelClass));
        this.querierOperator = new Operator<>(modelClass, new QuerierMapper(modelClass));
        this.readerOperator = new Operator<>(modelClass, new ReaderMapper(modelClass));
        this.updaterOperator = new Operator<>(modelClass, new UpdaterMapper(modelClass));
    }

    @Override
    public QueriedDocuments<MODEL> find(QuerierConsumer<MODEL> querierConsumer) {
        MODEL querier = QueryProxy.create(querierOperator.getRootClass(), querierOperator.getMapper().get());
        querierConsumer.accept(querier);

        return new QueriedDocumentsImpl<>(readerOperator, updaterOperator, dbCollection,
                QueryProxy.extract(querier).toDBObject());
    }

    @Override
    public QueriedDocuments<MODEL> find() {
        return new QueriedDocumentsImpl<>(readerOperator, updaterOperator, dbCollection, null);
    }

    @Override
    public String insert(InserterConsumer<MODEL> consumer) {
        MODEL inserter = InsertProxy.create(inserterOperator.getRootClass(), inserterOperator.getMapper().get());
        consumer.accept(inserter);

        BasicDBObject document = InsertProxy.extract(inserter).getDocument();

        if (preInsertTransformer != null){
            document = preInsertTransformer.transform(document);
        }

        dbCollection.insert(document);
        return document.getObjectId("_id").toHexString();
    }

    public void preInsert(DBObjectTransformer preInsertTransformer) {
        this.preInsertTransformer = preInsertTransformer;
    }
}
