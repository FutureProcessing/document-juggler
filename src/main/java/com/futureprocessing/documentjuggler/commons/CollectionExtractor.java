package com.futureprocessing.documentjuggler.commons;

import com.futureprocessing.documentjuggler.annotation.CollectionName;
import com.futureprocessing.documentjuggler.exception.validation.InvalidCollectionException;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public abstract class CollectionExtractor {
    public static DBCollection getDBCollection(DB db, Class model) {
        String collectionName = getCollectionName(model);
        return db.getCollection(collectionName);
    }

    static String getCollectionName(Class model) {
        if (model.isAnnotationPresent(CollectionName.class)) {
            CollectionName annotation = (CollectionName) model.getAnnotation(CollectionName.class);
            return annotation.value();
        }
        throw new InvalidCollectionException("Unknown DBCollection, " + CollectionName.class.getName() + " annotation was not found.");
    }
}
