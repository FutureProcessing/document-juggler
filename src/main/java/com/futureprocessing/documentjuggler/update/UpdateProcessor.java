package com.futureprocessing.documentjuggler.update;


import com.futureprocessing.documentjuggler.Operator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class UpdateProcessor<MODEL> {

    private final Operator<MODEL, UpdaterMapper> operator;
    private final DBCollection collection;

    public UpdateProcessor(DBCollection collection, Operator<MODEL, UpdaterMapper> operator) {
        this.collection = collection;
        this.operator = operator;
    }

    public UpdateProcessResult process(UpdaterConsumer<MODEL> consumer) {
        MODEL updater = UpdateProxy.create(operator.getRootClass(), operator.getMapper().get(), new RootUpdateBuilder());
        consumer.accept(updater);

        return new UpdateProcessResult(collection, UpdateProxy.extract(updater).getUpdateDocument());
    }

}
