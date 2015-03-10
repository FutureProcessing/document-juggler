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

public class Repository<INSERTER, QUERIER, READER, UPDATER> {

    private final Operator<INSERTER, InserterMapper> inserterOperator;
    private final Operator<QUERIER, QuerierMapper> querierOperator;
    private final Operator<READER, ReaderMapper> readerOperator;
    private final Operator<UPDATER, UpdaterMapper> updaterOperator;

    private final DBCollection dbCollection;

    public Repository(DBCollection dbCollection,
                      Class<INSERTER> inserterClass,
                      Class<QUERIER> querierClass,
                      Class<READER> readerClass,
                      Class<UPDATER> updaterClass) {
        this.dbCollection = dbCollection;

        MappingMode mappingMode = inserterClass != querierClass ||
                inserterClass != readerClass ||
                inserterClass != updaterClass ? MappingMode.STRICT : MappingMode.LENIENT;

        this.inserterOperator = new Operator<>(inserterClass, new InserterMapper(inserterClass, mappingMode));
        this.querierOperator = new Operator<>(querierClass, new QuerierMapper(querierClass, mappingMode));
        this.readerOperator = new Operator<>(readerClass, new ReaderMapper(readerClass, mappingMode));
        this.updaterOperator = new Operator<>(updaterClass, new UpdaterMapper(updaterClass, mappingMode));
    }

    public QueriedDocuments<READER, UPDATER> find(QuerierConsumer<QUERIER> querierConsumer) {
        QUERIER querier = QueryProxy.create(querierOperator.getRootClass(), querierOperator.getMapper().get());
        querierConsumer.accept(querier);

        return new QueriedDocuments<>(readerOperator, updaterOperator, dbCollection,
                QueryProxy.extract(querier).toDBObject());
    }

    public QueriedDocuments<READER, UPDATER> find() {
        return new QueriedDocuments<>(readerOperator, updaterOperator, dbCollection, null);
    }

    public String insert(InserterConsumer<INSERTER> consumer) {
        INSERTER inserter = InsertProxy.create(inserterOperator.getRootClass(), inserterOperator.getMapper().get());
        consumer.accept(inserter);

        BasicDBObject document = InsertProxy.extract(inserter).getDocument();
        dbCollection.insert(document);
        return document.getObjectId("_id").toHexString();
    }

}
