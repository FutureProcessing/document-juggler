package com.futureprocessing.documentjuggler.read;

import com.mongodb.*;

import java.util.*;

import static java.util.Collections.addAll;
import static java.util.Collections.unmodifiableSet;

public class ReadProcessor<MODEL> {

    private final Class<MODEL> modelClass;
    private final ReadMapper mapper;
    private final DBCollection collection;

    public ReadProcessor(Class<MODEL> modelClass, DBCollection collection) {
        this.modelClass = modelClass;
        this.collection = collection;
        this.mapper = new ReadMapper(modelClass);
    }

    public MODEL processFirst(DBObject query, String... fieldsToFetch) {
        Set<String> fields = toSet(fieldsToFetch);
        DBObject projection = getProjection(fields);

        BasicDBObject dbObject = (BasicDBObject) collection.findOne(query, projection);

        if (dbObject == null) {
            return null;
        }

        return createReadProxy(dbObject, fields);
    }

    public List<MODEL> processAll(DBObject query, OptionalInt skip, OptionalInt limit, String... fieldsToFetch) {
        Set<String> fields = toSet(fieldsToFetch);
        DBObject projection = getProjection(fields);

        List<MODEL> list = new ArrayList<>();

        try (DBCursor cursor = collection.find(query, projection)) {
            if (skip.isPresent()) {
                cursor.skip(skip.getAsInt());
            }
            if (limit.isPresent()) {
                cursor.limit(limit.getAsInt());
            }
            while (cursor.hasNext()) {
                DBObject document = cursor.next();
                list.add(createReadProxy(document, fields));
            }
        }

        return list;
    }

    private MODEL createReadProxy(DBObject dbObject, Set<String> fields) {
        return ReadProxy.create(modelClass, mapper.get(), dbObject, fields);
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
