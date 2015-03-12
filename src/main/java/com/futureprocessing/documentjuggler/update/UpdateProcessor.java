package com.futureprocessing.documentjuggler.update;


import com.mongodb.BasicDBObject;

public class UpdateProcessor<MODEL> {

    private final Class<MODEL> modelClass;
    private final UpdateMapper mapper;

    public UpdateProcessor(Class<MODEL> modelClass) {
        this.modelClass = modelClass;
        this.mapper = new UpdateMapper(modelClass);
    }

    public BasicDBObject process(UpdatConsumer<MODEL> consumer) {
        MODEL updater = UpdateProxy.create(modelClass, mapper.get(), new RootUpdateBuilder());
        consumer.accept(updater);

        return UpdateProxy.extract(updater).getUpdateDocument();
    }

}
