package com.futureprocessing.documentjuggler;

import com.mongodb.BasicDBObject;

@FunctionalInterface
public interface DBObjectTransformer {

    public BasicDBObject transform(BasicDBObject document);
}
