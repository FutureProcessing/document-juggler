package com.futureprocessing.documentjuggler.query;

import com.futureprocessing.documentjuggler.exception.LimitAlreadyPresentException;
import com.futureprocessing.documentjuggler.exception.SkipAlreadyPresentException;
import com.futureprocessing.documentjuggler.read.ReadProcessor;
import com.futureprocessing.documentjuggler.update.RemoveResult;
import com.futureprocessing.documentjuggler.update.UpdateConsumer;
import com.futureprocessing.documentjuggler.update.UpdateProcessor;
import com.futureprocessing.documentjuggler.update.BaseUpdateResult;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

import java.util.List;
import java.util.OptionalInt;

import static java.util.OptionalInt.empty;
import static java.util.OptionalInt.of;

public class QueriedDocumentsImpl<MODEL> implements QueriedDocuments<MODEL> {

    private final DBCollection collection;
    private final DBObject query;
    private final ReadProcessor<MODEL> readProcessor;
    private final UpdateProcessor<MODEL> updateProcessor;

    private OptionalInt skip = empty();
    private OptionalInt limit = empty();

    public QueriedDocumentsImpl(DBCollection collection, DBObject query,
                                ReadProcessor<MODEL> readProcessor, UpdateProcessor<MODEL> updateProcessor) {
        this.collection = collection;
        this.query = query;
        this.readProcessor = readProcessor;
        this.updateProcessor = updateProcessor;
    }

    @Override
    public MODEL first(String... fieldsToFetch) {
        return readProcessor.processFirst(query, fieldsToFetch);
    }

    @Override
    public List<MODEL> all(String... fieldsToFetch) {
        return readProcessor.processAll(query, skip, limit, fieldsToFetch);
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
    public BaseUpdateResult update(UpdateConsumer<MODEL> consumer) {
        BasicDBObject document = updateProcessor.process(consumer);
        WriteResult result = collection.update(query, document);
        return new BaseUpdateResult(result);
    }

    @Override
    public RemoveResult remove() {
        WriteResult result = collection.remove(query);
        return new RemoveResult(result);
    }
}
