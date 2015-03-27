package com.futureprocessing.documentjuggler.insert.command;


import com.futureprocessing.documentjuggler.commons.Mapper;
import com.futureprocessing.documentjuggler.insert.InsertProxy;
import com.mongodb.BasicDBObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EmbeddedVarArgInsertCommand extends AbstractInsertCommand {

    private final Class<?> embeddedType;
    private final Mapper<InsertCommand> mapper;

    public EmbeddedVarArgInsertCommand(String field, Class<?> embeddedType, Mapper<InsertCommand> mapper) {
        super(field);
        this.embeddedType = embeddedType;
        this.mapper = mapper;
    }

    @Override
    public void insert(BasicDBObject document, Object[] args) {
        Consumer[] consumers = (Consumer[]) args[0];

        List<BasicDBObject> documents = new ArrayList<>();
        for (Consumer consumer : consumers) {
            Object embedded = InsertProxy.create(embeddedType, mapper);
            consumer.accept(embedded);
            documents.add(InsertProxy.extract(embedded).getDocument());
        }

        document.put(field, documents);
    }

}
