package com.futureprocessing.documentjuggler.insert;


import com.mongodb.BasicDBObject;

import java.util.function.Consumer;

public class InsertProcessor<MODEL> {

    private final Class<MODEL> modelClass;
    private final InsertMapper mapper;

    public InsertProcessor(Class<MODEL> modelClass) {
        this.modelClass = modelClass;
        this.mapper = InsertMapper.map(modelClass);
    }

    public BasicDBObject process(Consumer<MODEL> consumer) {
        MODEL inserter = InsertProxy.create(modelClass, mapper);
        consumer.accept(inserter);

        return InsertProxy.extract(inserter).getDocument();
    }
}
