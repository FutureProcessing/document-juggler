package com.futureprocessing.mongojuggler;

import com.mongodb.DBCollection;

public class SimpleModelRepository<MODEL> extends Repository<MODEL, MODEL, MODEL, MODEL> {

    public SimpleModelRepository(DBCollection dbCollection, Class<MODEL> modelClass) {
        super(dbCollection, modelClass, modelClass, modelClass, modelClass);
    }
}
