package com.futureprocessing.mongojuggler.write;

import com.futureprocessing.mongojuggler.commons.ProxyCreator;
import com.futureprocessing.mongojuggler.exception.MissingPropertyException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

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

    public UpdateResult with(Consumer<UPDATER> consumer) {
        DBCollection collection = dbCollection;

        UPDATER updater = ProxyCreator.newUpdateProxy(updaterClass, new RootUpdateBuilder());
        consumer.accept(updater);

        BasicDBObject document = extractUpdateProxy(updater).getUpdateDocument();
        if(document.isEmpty()) {
            throw new MissingPropertyException("No property to update specified");
        }
        WriteResult result = collection.update(query, document);
        return new UpdateResult(result);
    }
}
