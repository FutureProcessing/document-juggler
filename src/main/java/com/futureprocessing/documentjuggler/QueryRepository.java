package com.futureprocessing.documentjuggler;

import com.futureprocessing.documentjuggler.commons.CollectionExtractor;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class QueryRepository<MODEL, QUERIER> extends AbstractRepository<MODEL, QUERIER> {

    public QueryRepository(DB db, Class<MODEL> modelClass, Class<QUERIER> querierClass) {
        this(CollectionExtractor.getDBCollection(db, modelClass), modelClass, querierClass);
    }

    public QueryRepository(DBCollection dbCollection, Class<MODEL> modelClass, Class<QUERIER> querierClass) {
        super(dbCollection, modelClass, querierClass);
    }
}
