package com.futureprocessing.mongojuggler;


import com.futureprocessing.mongojuggler.insert.InsertMapper;
import com.futureprocessing.mongojuggler.insert.InsertProxy;
import com.futureprocessing.mongojuggler.insert.InserterConsumer;
import com.futureprocessing.mongojuggler.query.QueriedDocuments;
import com.futureprocessing.mongojuggler.query.QuerierConsumer;
import com.futureprocessing.mongojuggler.query.QueryMapper;
import com.futureprocessing.mongojuggler.query.QueryProxy;
import com.futureprocessing.mongojuggler.read.ReadMapper;
import com.futureprocessing.mongojuggler.update.UpdateMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class Repository<READER, UPDATER, QUERIER, INSERTER> {

    private final Class<READER> readerClass;
    private final Class<UPDATER> updaterClass;
    private final Class<INSERTER> inserterClass;
    private final Class<QUERIER> queryClass;

    private final DBCollection dbCollection;

    private final QueryMapper queryMapper;
    private final ReadMapper readMapper;
    private final InsertMapper insertMapper;
    private final UpdateMapper updateMapper;

    public Repository(Class<READER> readerClass, Class<UPDATER> updaterClass, Class<QUERIER> querierClass,
                      Class<INSERTER> inserterClass,
                      DBCollection dbCollection) {
        this.readerClass = readerClass;
        this.updaterClass = updaterClass;
        this.inserterClass = inserterClass;
        this.queryClass = querierClass;

        this.dbCollection = dbCollection;

        queryMapper = new QueryMapper(querierClass);
        readMapper = new ReadMapper(readerClass);
        insertMapper = new InsertMapper(inserterClass);
        updateMapper = new UpdateMapper(updaterClass);
    }

    public QueriedDocuments<READER, UPDATER> find(QuerierConsumer<QUERIER> querierConsumer) {
        QUERIER querier = QueryProxy.create(queryClass, queryMapper.get(queryClass));
        querierConsumer.accept(querier);

        return new QueriedDocuments<>(readerClass, updaterClass, readMapper, updateMapper, dbCollection,
                                      QueryProxy.extract(querier).toDBObject());
    }

    public QueriedDocuments<READER, UPDATER> find() {
        return new QueriedDocuments<>(readerClass, updaterClass, readMapper, updateMapper, dbCollection, null);
    }

    public String insert(InserterConsumer<INSERTER> consumer) {
        INSERTER inserter = InsertProxy.create(inserterClass, insertMapper.get(inserterClass));
        consumer.accept(inserter);

        BasicDBObject document = InsertProxy.extract(inserter).getDocument();
        dbCollection.insert(document);
        return document.getObjectId("_id").toHexString();
    }


}
