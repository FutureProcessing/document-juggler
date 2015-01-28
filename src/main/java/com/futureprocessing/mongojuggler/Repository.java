package com.futureprocessing.mongojuggler;


import com.futureprocessing.mongojuggler.commons.Metadata;
import com.futureprocessing.mongojuggler.commons.ProxyCreator;
import com.futureprocessing.mongojuggler.commons.ProxyExtractor;
import com.futureprocessing.mongojuggler.read.LambdaReader;
import com.futureprocessing.mongojuggler.write.LambdaUpdater;
import com.mongodb.DBCollection;

import java.util.function.Consumer;

public abstract class Repository<READER, UPDATER, QUERY> {

    private final Class<READER> readerClass;
    private final Class<UPDATER> updaterClass;
    private final MongoDBProvider dbProvider;
    private final Class<QUERY> queryClass;

    public Repository(Class<READER> readerClass, Class<UPDATER> updaterClass, Class<QUERY> queryClass, MongoDBProvider dbProvider) {
        this.readerClass = readerClass;
        this.updaterClass = updaterClass;
        this.dbProvider = dbProvider;
        this.queryClass = queryClass;
    }

    public LambdaReader<READER> find(Consumer<QUERY> queryConsumer) {
        QUERY query = ProxyCreator.newQueryProxy(queryClass);
        queryConsumer.accept(query);

        LambdaReader<READER> lambdaReader = new LambdaReader<>(readerClass, getDBCollection(),
                ProxyExtractor.extractQueryProxy(query).toDBObject());
        return lambdaReader;

    }

    public String insert(Consumer<UPDATER> consumer) {
        DBCollection collection = getDBCollection();
        UPDATER updater = ProxyCreator.newInsertProxy(updaterClass, collection);
        consumer.accept(updater);
        return ProxyExtractor.extractInsertProxy(updater).execute();
    }

    private DBCollection getDBCollection() {
        String collectionName = Metadata.getCollectionName(updaterClass);
        return dbProvider.db().getCollection(collectionName);
    }

    public LambdaUpdater<UPDATER> update(Consumer<QUERY> consumer) {
        QUERY query = ProxyCreator.newQueryProxy(queryClass);
        consumer.accept(query);

        LambdaUpdater<UPDATER> lambdaUpdater = new LambdaUpdater<>(updaterClass, getDBCollection(),
                ProxyExtractor.extractQueryProxy(query).toDBObject());
        return lambdaUpdater;
    }

}
