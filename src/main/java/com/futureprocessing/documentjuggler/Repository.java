package com.futureprocessing.documentjuggler;


import com.futureprocessing.documentjuggler.commons.CollectionExtractor;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class Repository<MODEL> extends AbstractRepository<MODEL, MODEL> {


    public Repository(DB db, Class<MODEL> modelClass) {
        this(CollectionExtractor.getDBCollection(db, modelClass), modelClass);
    }

    public Repository(DBCollection dbCollection, Class<MODEL> modelClass) {
        super(dbCollection, modelClass, modelClass);
    }
}
