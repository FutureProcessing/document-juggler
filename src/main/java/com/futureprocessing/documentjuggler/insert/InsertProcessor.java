package com.futureprocessing.documentjuggler.insert;


import com.futureprocessing.documentjuggler.Operator;
import com.mongodb.DBCollection;

public class InsertProcessor<MODEL> {

    private final Operator<MODEL, InserterMapper> operator;
    private final DBCollection collection;

    public InsertProcessor(DBCollection collection, Operator<MODEL, InserterMapper> operator) {
        this.collection = collection;
        this.operator = operator;
    }

    public InsertProcessResult process(InserterConsumer<MODEL> consumer) {
        MODEL inserter = InsertProxy.create(operator.getRootClass(), operator.getMapper().get());
        consumer.accept(inserter);

        return new InsertProcessResult(collection, InsertProxy.extract(inserter).getDocument());
    }

}
