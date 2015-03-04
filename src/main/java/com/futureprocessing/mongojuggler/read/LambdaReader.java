package com.futureprocessing.mongojuggler.read;

import com.futureprocessing.mongojuggler.exception.LimitAlreadyPresentException;
import com.futureprocessing.mongojuggler.exception.MissingPropertyException;
import com.futureprocessing.mongojuggler.exception.SkipAlreadyPresentException;
import com.futureprocessing.mongojuggler.update.RootUpdateBuilder;
import com.futureprocessing.mongojuggler.update.UpdateMapper;
import com.futureprocessing.mongojuggler.update.UpdateProxy;
import com.futureprocessing.mongojuggler.update.UpdateResult;
import com.mongodb.*;

import java.util.*;
import java.util.function.Consumer;

import static java.util.Collections.addAll;
import static java.util.Collections.unmodifiableSet;
import static java.util.OptionalInt.empty;
import static java.util.OptionalInt.of;

public class LambdaReader<READER, UPDATER> {

    private final Class<READER> readerClass;
    private final Class<UPDATER> updaterClass;
    private final ReadMapper readMapper;
    private final UpdateMapper updateMapper;
    private final DBCollection dbCollection;
    private final DBObject query;
    private OptionalInt skip = empty();
    private OptionalInt limit = empty();

    public LambdaReader(Class<READER> readerClass, Class<UPDATER> updaterClass,
                        ReadMapper readMapper, UpdateMapper updateMapper,
                        DBCollection dbCollection, DBObject query) {
        this.readerClass = readerClass;
        this.readMapper = readMapper;
        this.updaterClass = updaterClass;
        this.updateMapper = updateMapper;
        this.dbCollection = dbCollection;
        this.query = query;
    }

    public READER first(String... fieldsToFetch) {
        Set<String> fields = toSet(fieldsToFetch);
        DBObject projection = getProjection(fields);

        BasicDBObject dbObject = (BasicDBObject) dbCollection.findOne(query, projection);

        return ReadProxy.create(readerClass, readMapper.get(readerClass), dbObject, fields);
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
                list.add(ReadProxy.create(readerClass, readMapper.get(readerClass), document, fields));
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

    public LambdaReader<READER, UPDATER> skip(int skip) {
        if (this.skip.isPresent()) {
            throw new SkipAlreadyPresentException();
        }
        this.skip = of(skip);
        return this;
    }

    public LambdaReader<READER, UPDATER> limit(int limit) {
        if (this.limit.isPresent()) {
            throw new LimitAlreadyPresentException();
        }
        this.limit = of(limit);
        return this;
    }

    public UpdateResult update(Consumer<UPDATER> consumer) {
        DBCollection collection = dbCollection;

        UPDATER updater = UpdateProxy.create(updaterClass, updateMapper.get(updaterClass), new RootUpdateBuilder());
        consumer.accept(updater);

        BasicDBObject document = UpdateProxy.extract(updater).getUpdateDocument();
        if (document.isEmpty()) {
            throw new MissingPropertyException("No property to update specified");
        }
        WriteResult result = collection.update(query, document);
        return new UpdateResult(result);
    }
}
