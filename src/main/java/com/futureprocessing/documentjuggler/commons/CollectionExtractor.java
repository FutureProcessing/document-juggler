package com.futureprocessing.documentjuggler.commons;

import com.futureprocessing.documentjuggler.annotation.DbCollection;
import com.futureprocessing.documentjuggler.exception.validation.InvalidCollectionException;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public abstract class CollectionExtractor {
    public static DBCollection getDBCollection(DB db, Class model) {
        String collectionName = getCollectionName(model);
        return db.getCollection(collectionName);
    }

    static String getCollectionName(Class model) {
        if (model.isAnnotationPresent(DbCollection.class)) {
            DbCollection annotation = (DbCollection) model.getAnnotation(DbCollection.class);
            return annotation.value();
        }
        throw new InvalidCollectionException("Unknown DBCollection, " + DbCollection.class.getName() + " annotation was not found.");
    }
}
