package com.futureprocessing.mongojuggler;


import com.futureprocessing.mongojuggler.insert.InsertProxy;
import com.futureprocessing.mongojuggler.insert.InserterConsumer;
import com.futureprocessing.mongojuggler.insert.InserterMapper;
import com.futureprocessing.mongojuggler.query.QueriedDocuments;
import com.futureprocessing.mongojuggler.query.QuerierConsumer;
import com.futureprocessing.mongojuggler.query.QuerierMapper;
import com.futureprocessing.mongojuggler.query.QueryProxy;
import com.futureprocessing.mongojuggler.read.ReaderMapper;
import com.futureprocessing.mongojuggler.update.UpdaterMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class Repository<MODEL> {

    private final Operator<MODEL, InserterMapper> inserterOperator;
    private final Operator<MODEL, QuerierMapper> querierOperator;
    private final Operator<MODEL, ReaderMapper> readerOperator;
    private final Operator<MODEL, UpdaterMapper> updaterOperator;

    private final DBCollection dbCollection;

    public Repository(DBCollection dbCollection, Class<MODEL> modelClass) {
        this.dbCollection = dbCollection;

        MappingMode mappingMode = MappingMode.LENIENT;

        this.inserterOperator = new Operator<>(modelClass, new InserterMapper(modelClass, mappingMode));
        this.querierOperator = new Operator<>(modelClass, new QuerierMapper(modelClass, mappingMode));
        this.readerOperator = new Operator<>(modelClass, new ReaderMapper(modelClass, mappingMode));
        this.updaterOperator = new Operator<>(modelClass, new UpdaterMapper(modelClass, mappingMode));
    }

    public QueriedDocuments<MODEL> find(QuerierConsumer<MODEL> querierConsumer) {
        MODEL querier = QueryProxy.create(querierOperator.getRootClass(), querierOperator.getMapper().get());
        querierConsumer.accept(querier);

        return new QueriedDocuments<>(readerOperator, updaterOperator, dbCollection,
                QueryProxy.extract(querier).toDBObject());
    }

    public QueriedDocuments<MODEL> find() {
        return new QueriedDocuments<>(readerOperator, updaterOperator, dbCollection, null);
    }

    public String insert(InserterConsumer<MODEL> consumer) {
        MODEL inserter = InsertProxy.create(inserterOperator.getRootClass(), inserterOperator.getMapper().get());
        consumer.accept(inserter);

        BasicDBObject document = InsertProxy.extract(inserter).getDocument();
        dbCollection.insert(document);
        return document.getObjectId("_id").toHexString();
    }

}
