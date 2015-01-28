package com.futureprocessing.mongojuggler.read;

import com.mongodb.*;

import java.util.*;

import static com.futureprocessing.mongojuggler.commons.ProxyCreator.newReadProxy;
import static java.util.Collections.addAll;
import static java.util.Collections.unmodifiableSet;
import static java.util.OptionalInt.empty;
import static java.util.OptionalInt.of;

public class LambdaReader<READER> {

    private final Class<READER> readerClass;
    private final DBCollection dbCollection;
    private final DBObject query;
    private OptionalInt skip = empty();
    private OptionalInt limit = empty();

    public LambdaReader(Class<READER> readerClass, DBCollection dbCollection, DBObject query) {
        this.readerClass = readerClass;
        this.dbCollection = dbCollection;
        this.query = query;
    }

    public READER one(String... fieldsToFetch) {
        Set<String> fields = toSet(fieldsToFetch);
        DBObject projection = getProjection(fields);

        BasicDBObject dbObject = (BasicDBObject) dbCollection.findOne(query, projection);

        return newReadProxy(readerClass, dbObject, fields);
    }

    public List<READER> all(String... fieldsToFetch) {
        Set<String> fields = toSet(fieldsToFetch);
        DBObject projection = getProjection(fields);

        List<READER> list = new ArrayList<>();

        try (DBCursor cursor = dbCollection.find(query, projection)) {
            if (skip.isPresent()) {
                cursor.skip(skip.getAsInt());
            }
            if (limit.isPresent()) {
                cursor.limit(limit.getAsInt());
            }
            while (cursor.hasNext()) {
                DBObject document = cursor.next();
                list.add(newReadProxy(readerClass, document, fields));
            }
        }

        return list;
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

    public LambdaReader<READER> skip(int skip) {
        this.skip = of(skip);
        return this;
    }

    public LambdaReader<READER> limit(int limit) {
        this.limit = of(limit);
        return this;
    }
}
