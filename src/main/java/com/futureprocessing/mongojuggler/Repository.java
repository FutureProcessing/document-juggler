package com.futureprocessing.mongojuggler;


import com.futureprocessing.mongojuggler.commons.Metadata;
import com.futureprocessing.mongojuggler.commons.ProxyCreator;
import com.futureprocessing.mongojuggler.commons.ProxyExtractor;
import com.futureprocessing.mongojuggler.read.LambdaReader;
import com.futureprocessing.mongojuggler.read.QueryMapper;
import com.futureprocessing.mongojuggler.read.ReadMapper;
import com.futureprocessing.mongojuggler.write.InsertMapper;
import com.futureprocessing.mongojuggler.write.LambdaUpdater;
import com.futureprocessing.mongojuggler.write.UpdateMapper;
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

    public Repository(Class<READER> readerClass, Class<UPDATER> updaterClass, Class<QUERY> queryClass, MongoDBProvider dbProvider) {
        this.readerClass = readerClass;
        this.updaterClass = updaterClass;
        this.dbProvider = dbProvider;
        this.queryClass = queryClass;

        queryMapper = new QueryMapper(queryClass);
        readMapper = new ReadMapper(readerClass);
        insertMapper = new InsertMapper(updaterClass);
        updateMapper = new UpdateMapper(updaterClass);
    }

    public LambdaReader<READER> find(Consumer<QUERY> queryConsumer) {
        QUERY query = ProxyCreator.newQueryProxy(queryClass, queryMapper);
        queryConsumer.accept(query);

        LambdaReader<READER> lambdaReader = new LambdaReader<>(readerClass, readMapper, getDBCollection(),
                ProxyExtractor.extractQueryProxy(query).toDBObject());
        return lambdaReader;

    }

    public LambdaReader<READER> find() {
        return new LambdaReader<>(readerClass, readMapper, getDBCollection(), null);
    }

    public String insert(Consumer<UPDATER> consumer) {
        DBCollection collection = getDBCollection();
        UPDATER updater = ProxyCreator.newInsertProxy(updaterClass, insertMapper);
        consumer.accept(updater);

        BasicDBObject document = ProxyExtractor.extractInsertProxy(updater).getDocument();
        collection.insert(document);
        return document.getObjectId("_id").toHexString();
    }

    private DBCollection getDBCollection() {
        String collectionName = Metadata.getCollectionName(updaterClass);
        return dbProvider.db().getCollection(collectionName);
    }

    public LambdaUpdater<UPDATER> update(Consumer<QUERY> consumer) {
        QUERY query = ProxyCreator.newQueryProxy(queryClass, queryMapper);
        consumer.accept(query);

        LambdaUpdater<UPDATER> lambdaUpdater = new LambdaUpdater<>(updaterClass, updateMapper, getDBCollection(),
                ProxyExtractor.extractQueryProxy(query).toDBObject());
        return lambdaUpdater;
    }

}
