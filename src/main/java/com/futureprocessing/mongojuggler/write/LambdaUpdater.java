package com.futureprocessing.mongojuggler.write;

import com.futureprocessing.mongojuggler.commons.ProxyCreator;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import java.util.function.Consumer;

import static com.futureprocessing.mongojuggler.commons.ProxyExtractor.extractUpdateProxy;

public class LambdaUpdater<UPDATER> {

    private final DBObject query;
    private final Class<UPDATER> updaterClass;
    private final DBCollection dbCollection;

    public LambdaUpdater(Class<UPDATER> updaterClass, DBCollection dbCollection, DBObject query) {
        this.updaterClass = updaterClass;
        this.dbCollection = dbCollection;
        this.query = query;
    }

    public void with(Consumer<UPDATER> consumer) {
        DBCollection collection = dbCollection;

        UPDATER updater = ProxyCreator.newUpdateProxy(updaterClass, collection, query);
        consumer.accept(updater);

        extractUpdateProxy(updater).execute();
    }
}