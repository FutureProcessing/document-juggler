package com.futureprocessing.mongojuggler;


import com.futureprocessing.mongojuggler.commons.Metadata;
import com.futureprocessing.mongojuggler.insert.InsertMapper;
import com.futureprocessing.mongojuggler.insert.InsertProxy;
import com.futureprocessing.mongojuggler.query.QueriedDocuments;
import com.futureprocessing.mongojuggler.query.QueryConsumer;
import com.futureprocessing.mongojuggler.query.QueryMapper;
import com.futureprocessing.mongojuggler.query.QueryProxy;
import com.futureprocessing.mongojuggler.read.ReadMapper;
import com.futureprocessing.mongojuggler.update.UpdateMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import java.util.function.Consumer;

public class Repository<READER, UPDATER, QUERY, INSERTER> {

    private final Class<READER> readerClass;
    private final Class<UPDATER> updaterClass;
    private final Class<INSERTER> inserterClass;
    private final MongoDBProvider dbProvider;
    private final Class<QUERY> queryClass;

    private final QueryMapper queryMapper;
    private final ReadMapper readMapper;
    private final InsertMapper insertMapper;
    private final UpdateMapper updateMapper;

    public Repository(Class<READER> readerClass, Class<UPDATER> updaterClass, Class<QUERY> querierClass, Class<INSERTER> inserterClass,
                      MongoDBProvider dbProvider) {
        this.readerClass = readerClass;
        this.updaterClass = updaterClass;
        this.inserterClass = inserterClass;
        this.dbProvider = dbProvider;
        this.queryClass = querierClass;

        queryMapper = new QueryMapper(querierClass);
        readMapper = new ReadMapper(readerClass);
        insertMapper = new InsertMapper(inserterClass);
        updateMapper = new UpdateMapper(updaterClass);
    }

    public QueriedDocuments<READER, UPDATER> find(QueryConsumer<QUERY> queryConsumer) {
        QUERY query = QueryProxy.create(queryClass, queryMapper.get(queryClass));
        queryConsumer.accept(query);

        return new QueriedDocuments<>(readerClass, updaterClass, readMapper, updateMapper, getDBCollection(),
                                      QueryProxy.extract(query).toDBObject());
    }

    public QueriedDocuments<READER, UPDATER> find() {
        return new QueriedDocuments<>(readerClass, updaterClass, readMapper, updateMapper, getDBCollection(), null);
    }

    public String insert(Consumer<INSERTER> consumer) {
        DBCollection collection = getDBCollection();
        INSERTER inserter = InsertProxy.create(inserterClass, insertMapper.get(inserterClass));
        consumer.accept(inserter);

        BasicDBObject document = InsertProxy.extract(inserter).getDocument();
        collection.insert(document);
        return document.getObjectId("_id").toHexString();
    }

    private DBCollection getDBCollection() {
        String collectionName = Metadata.getCollectionName(updaterClass);
        return dbProvider.db().getCollection(collectionName);
    }

}
