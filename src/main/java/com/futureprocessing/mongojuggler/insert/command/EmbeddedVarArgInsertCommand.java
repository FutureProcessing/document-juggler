package com.futureprocessing.mongojuggler.insert.command;


import com.futureprocessing.mongojuggler.insert.InsertMapper;
import com.futureprocessing.mongojuggler.insert.InsertProxy;
import com.mongodb.BasicDBObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EmbeddedVarArgInsertCommand extends AbstractInsertCommand {

    private final Class<?> embeddedType;
    private final InsertMapper mapper;

    public EmbeddedVarArgInsertCommand(String field, Class<?> embeddedType, InsertMapper mapper) {
        super(field);
        this.embeddedType = embeddedType;
        this.mapper = mapper;
    }

    @Override
    public void insert(BasicDBObject document, Object[] args) {
        Object embedded = InsertProxy.create(embeddedType, mapper.get(embeddedType));
        Consumer[] consumers = (Consumer[]) args[0];

        List<BasicDBObject> documents = new ArrayList<>();
        for(Consumer consumer : consumers) {
            consumer.accept(embedded);
            documents.add(InsertProxy.extract(embedded).getDocument());
        }
        document.put(field, documents);
    }

}
