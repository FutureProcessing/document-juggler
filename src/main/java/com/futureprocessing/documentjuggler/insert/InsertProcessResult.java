package com.futureprocessing.documentjuggler.insert;


import com.futureprocessing.documentjuggler.DBObjectTransformer;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class InsertProcessResult {

    private final DBCollection collection;
    private final BasicDBObject document;

    public InsertProcessResult(DBCollection collection, BasicDBObject document) {
        this.collection = collection;
        this.document = document;
    }

    public InsertProcessResult transform(DBObjectTransformer transformer) {
        return new InsertProcessResult(collection, transformer.transform(document));
    }

    public String execute() {
        collection.insert(document);
        return document.getObjectId("_id").toHexString();
    }
}
