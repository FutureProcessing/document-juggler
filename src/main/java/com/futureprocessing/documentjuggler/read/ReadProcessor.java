package com.futureprocessing.documentjuggler.read;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.util.*;

import static java.util.Collections.addAll;
import static java.util.Collections.unmodifiableSet;

public class ReadProcessor<MODEL> {

    private final Class<MODEL> modelClass;
    private final ReadMapper mapper;
    private final DBCollection collection;
    private final ProjectionCreator projectionCreator;

    public ReadProcessor(Class<MODEL> modelClass, DBCollection collection) {
        this.modelClass = modelClass;
        this.collection = collection;
        this.mapper = ReadMapper.map(modelClass);
        this.projectionCreator = ProjectionCreator.create(this.mapper);
    }

    public MODEL processFirst(DBObject query, String... fieldsToFetch) {
        Set<String> fields = toSet(fieldsToFetch);
        DBObject projection = projectionCreator.getProjection(fields);

        BasicDBObject dbObject = (BasicDBObject) collection.findOne(query, projection);

        if (dbObject == null) {
            return null;
        }

        return createReadProxy(dbObject, fields);
    }

    public List<MODEL> processAll(DBObject query, OptionalInt skip, OptionalInt limit, String... fieldsToFetch) {
        Set<String> fields = toSet(fieldsToFetch);
        DBObject projection = projectionCreator.getProjection(fields);

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
        return ReadProxy.create(modelClass, mapper, dbObject, fields);
    }

    private Set<String> toSet(String... fields) {
        Set<String> set = new HashSet<>();
        addAll(set, fields);
        return unmodifiableSet(set);
    }
}
