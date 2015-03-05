package com.futureprocessing.mongojuggler.query;

import com.futureprocessing.mongojuggler.Operator;
import com.futureprocessing.mongojuggler.exception.LimitAlreadyPresentException;
import com.futureprocessing.mongojuggler.exception.MissingPropertyException;
import com.futureprocessing.mongojuggler.exception.SkipAlreadyPresentException;
import com.futureprocessing.mongojuggler.read.ReadProxy;
import com.futureprocessing.mongojuggler.read.ReaderMapper;
import com.futureprocessing.mongojuggler.update.*;
import com.mongodb.*;

import java.util.*;

import static java.util.Collections.addAll;
import static java.util.Collections.unmodifiableSet;
import static java.util.OptionalInt.empty;
import static java.util.OptionalInt.of;

public class QueriedDocuments<READER, UPDATER> implements ReadQueriedDocuments<READER> {

    private final Operator<READER, ReaderMapper> readerOperator;
    private final Operator<UPDATER, UpdaterMapper> updaterOperator;

    private final DBCollection dbCollection;
    private final DBObject query;
    private OptionalInt skip = empty();
    private OptionalInt limit = empty();


    public QueriedDocuments(Operator<READER, ReaderMapper> readerOperator,
                            Operator<UPDATER, UpdaterMapper> updaterOperator,
                            DBCollection dbCollection,
                            DBObject query) {
        this.readerOperator = readerOperator;
        this.updaterOperator = updaterOperator;

        this.dbCollection = dbCollection;
        this.query = query;
    }

    @Override
    public READER first(String... fieldsToFetch) {
        Set<String> fields = toSet(fieldsToFetch);
        DBObject projection = getProjection(fields);

        BasicDBObject dbObject = (BasicDBObject) dbCollection.findOne(query, projection);

        return createReadProxy(dbObject, fields);
    }

    private READER createReadProxy(DBObject dbObject, Set<String> fields) {
        return ReadProxy.create(readerOperator.getRootClass(), readerOperator.getMapper().get(), dbObject, fields);
    }

    @Override
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
                list.add(createReadProxy(document, fields));
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

    @Override
    public ReadQueriedDocuments<READER> skip(int skip) {
        if (this.skip.isPresent()) {
            throw new SkipAlreadyPresentException();
        }
        this.skip = of(skip);
        return this;
    }

    @Override
    public ReadQueriedDocuments<READER> limit(int limit) {
        if (this.limit.isPresent()) {
            throw new LimitAlreadyPresentException();
        }
        this.limit = of(limit);
        return this;
    }

    public UpdateResult update(UpdaterConsumer<UPDATER> consumer) {
        DBCollection collection = dbCollection;

        UPDATER updater = UpdateProxy.create(updaterOperator.getRootClass(), updaterOperator.getMapper().get(), new RootUpdateBuilder());
        consumer.accept(updater);

        BasicDBObject document = UpdateProxy.extract(updater).getUpdateDocument();
        if (document.isEmpty()) {
            throw new MissingPropertyException("No property to update specified");
        }
        WriteResult result = collection.update(query, document);
        return new UpdateResult(result);
    }
}
