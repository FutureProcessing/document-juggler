package com.futureprocessing.mongojuggler;


import com.futureprocessing.mongojuggler.commons.Metadata;
import com.futureprocessing.mongojuggler.insert.InsertMapper;
import com.futureprocessing.mongojuggler.insert.InsertProxy;
import com.futureprocessing.mongojuggler.query.QueryConsumer;
import com.futureprocessing.mongojuggler.query.QueryMapper;
import com.futureprocessing.mongojuggler.query.QueryProxy;
import com.futureprocessing.mongojuggler.read.LambdaReader;
import com.futureprocessing.mongojuggler.read.ReadMapper;
import com.futureprocessing.mongojuggler.update.UpdateMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import java.util.function.Consumer;

public class Repository<READER, UPDATER, QUERY> {

    private final Class<READER> readerClass;
    private final Class<UPDATER> updaterClass;
    private final MongoDBProvider dbProvider;
    private final Class<QUERY> queryClass;

    private final QueryMapper queryMapper;
    private final ReadMapper readMapper;
    private final InsertMapper insertMapper;
    private final UpdateMapper updateMapper;

    public Repository(Class<READER> readerClass, Class<UPDATER> updaterClass, Class<QUERY> queryClass,
                      MongoDBProvider dbProvider) {
        this.readerClass = readerClass;
        this.updaterClass = updaterClass;
        this.dbProvider = dbProvider;
        this.queryClass = queryClass;

        queryMapper = new QueryMapper(queryClass);
        readMapper = new ReadMapper(readerClass);
        insertMapper = new InsertMapper(updaterClass);
        updateMapper = new UpdateMapper(updaterClass);
    }

    public LambdaReader<READER, UPDATER> find(QueryConsumer<QUERY> queryConsumer) {
        QUERY query = QueryProxy.create(queryClass, queryMapper.get(queryClass));
        queryConsumer.accept(query);

        return new LambdaReader<>(readerClass, updaterClass, readMapper, updateMapper, getDBCollection(),
                                  QueryProxy.extract(query).toDBObject());
    }

    public LambdaReader<READER, UPDATER> find() {
        return new LambdaReader<>(readerClass, updaterClass, readMapper, updateMapper, getDBCollection(), null);
    }

    public String insert(Consumer<UPDATER> consumer) {
        DBCollection collection = getDBCollection();
        UPDATER updater = InsertProxy.create(updaterClass, insertMapper.get(updaterClass));
        consumer.accept(updater);

        BasicDBObject document = InsertProxy.extract(updater).getDocument();
        collection.insert(document);
        return document.getObjectId("_id").toHexString();
    }

    private DBCollection getDBCollection() {
        String collectionName = Metadata.getCollectionName(updaterClass);
        return dbProvider.db().getCollection(collectionName);
    }

}
