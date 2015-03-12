package com.futureprocessing.documentjuggler.update;


import com.futureprocessing.documentjuggler.DBObjectTransformer;
import com.futureprocessing.documentjuggler.exception.MissingPropertyException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class UpdateProcessResult {

    private final DBCollection collection;
    private final DBObject query;
    private final BasicDBObject document;

    public UpdateProcessResult(DBCollection collection, BasicDBObject document) {
        this(collection, null, document);
    }

    private UpdateProcessResult(DBCollection collection, DBObject query, BasicDBObject document) {
        this.collection = collection;
        this.document = document;
        this.query = query;
    }

    public UpdateProcessResult withQuery(DBObject query) {
        return new UpdateProcessResult(collection, query, document);
    }

    public UpdateProcessResult transform(DBObjectTransformer transformer) {
        return new UpdateProcessResult(collection, query, transformer.transform(document));
    }

    public UpdateResult execute() {
        if (document.isEmpty()) {
            throw new MissingPropertyException("No property to update specified");
        }
        WriteResult result = collection.update(query, document);
        return new UpdateResult(result);
    }
}
