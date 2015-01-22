package com.futureprocessing.mongojuggler.read;

import com.futureprocessing.mongojuggler.commons.ProxyCreator;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.addAll;
import static java.util.Collections.unmodifiableSet;

public class LambdaReader<READER> {

    private final Class<READER> readerClass;
    private final DBCollection dbCollection;
    private final DBObject query;

    public LambdaReader(Class<READER> readerClass, DBCollection dbCollection, DBObject query) {
        this.readerClass = readerClass;
        this.dbCollection = dbCollection;
        this.query = query;
    }

    public READER one(String... fieldsToFetch) {
        Set<String> fields = toSet(fieldsToFetch);
        DBObject projection = getProjection(fields);

        DBObject dbQuery = query;

        BasicDBObject dbObject = (BasicDBObject) dbCollection.findOne(dbQuery, projection);

        return ProxyCreator.newReadProxy(readerClass, dbObject, fields);
    }

    private Set<String> toSet(String... fields) {
        Set<String> set = new HashSet<>();
        addAll(set, fields);
        return unmodifiableSet(set);
    }

    private DBObject getProjection(Set<String> fields) {
        if (fields.isEmpty()) {
            return null;
        }
        BasicDBObjectBuilder start = BasicDBObjectBuilder.start();
        fields.forEach(field -> start.append(field, 1));
        return start.get();
    }

}
