package com.futureprocessing.documentjuggler.query;

import com.futureprocessing.documentjuggler.Operator;
import com.futureprocessing.documentjuggler.exception.LimitAlreadyPresentException;
import com.futureprocessing.documentjuggler.exception.MissingPropertyException;
import com.futureprocessing.documentjuggler.exception.SkipAlreadyPresentException;
import com.futureprocessing.documentjuggler.read.ReadProxy;
import com.futureprocessing.documentjuggler.read.ReaderMapper;
import com.futureprocessing.documentjuggler.update.*;
import com.mongodb.*;

import java.util.*;

import static java.util.Collections.addAll;
import static java.util.Collections.unmodifiableSet;
import static java.util.OptionalInt.empty;
import static java.util.OptionalInt.of;

public class QueriedDocumentsImpl<MODEL> implements QueriedDocuments<MODEL> {

    private final Operator<MODEL, ReaderMapper> readerOperator;
    private final Operator<MODEL, UpdaterMapper> updaterOperator;

    private final DBCollection dbCollection;
    private final DBObject query;
    private OptionalInt skip = empty();
    private OptionalInt limit = empty();

    public QueriedDocumentsImpl(Operator<MODEL, ReaderMapper> readerOperator,
                                Operator<MODEL, UpdaterMapper> updaterOperator,
                                DBCollection dbCollection,
                                DBObject query) {
        this.readerOperator = readerOperator;
        this.updaterOperator = updaterOperator;

        this.dbCollection = dbCollection;
        this.query = query;
    }

    @Override
    public MODEL first(String... fieldsToFetch) {
        Set<String> fields = toSet(fieldsToFetch);
        DBObject projection = getProjection(fields);

        BasicDBObject dbObject = (BasicDBObject) dbCollection.findOne(query, projection);

        if (dbObject == null) {
            return null;
        }

        return createReadProxy(dbObject, fields);
    }

    private MODEL createReadProxy(DBObject dbObject, Set<String> fields) {
        return ReadProxy.create(readerOperator.getRootClass(), readerOperator.getMapper().get(), dbObject, fields);
    }

    @Override
    public List<MODEL> all(String... fieldsToFetch) {
        Set<String> fields = toSet(fieldsToFetch);
        DBObject projection = getProjection(fields);

        List<MODEL> list = new ArrayList<>();

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
    public ReadQueriedDocuments<MODEL> skip(int skip) {
        if (this.skip.isPresent()) {
            throw new SkipAlreadyPresentException();
        }
        this.skip = of(skip);
        return this;
    }

    @Override
    public ReadQueriedDocuments<MODEL> limit(int limit) {
        if (this.limit.isPresent()) {
            throw new LimitAlreadyPresentException();
        }
        this.limit = of(limit);
        return this;
    }

    @Override
    public UpdateResult update(UpdaterConsumer<MODEL> consumer) {

        MODEL updater = UpdateProxy.create(updaterOperator.getRootClass(), updaterOperator.getMapper().get(), new RootUpdateBuilder());
        consumer.accept(updater);

        BasicDBObject document = UpdateProxy.extract(updater).getUpdateDocument();

        if (document.isEmpty()) {
            throw new MissingPropertyException("No property to update specified");
        }
        WriteResult result = dbCollection.update(query, document);
        return new UpdateResult(result);
    }

    @Override
    public RemoveResult remove() {
        WriteResult result = dbCollection.remove(query);
        return new RemoveResult(result);
    }
}
