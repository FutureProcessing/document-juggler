package com.futureprocessing.documentjuggler.insert;


import com.mongodb.BasicDBObject;

public class InsertProcessor<MODEL> {

    private final Class<MODEL> modelClass;
    private final InsertMapper mapper;

    public InsertProcessor(Class<MODEL> modelClass) {
        this.modelClass = modelClass;
        this.mapper = InsertMapper.map(modelClass);
    }

    public BasicDBObject process(InsertConsumer<MODEL> consumer) {
        MODEL inserter = InsertProxy.create(modelClass, mapper.get());
        consumer.accept(inserter);

        return InsertProxy.extract(inserter).getDocument();
    }
}
