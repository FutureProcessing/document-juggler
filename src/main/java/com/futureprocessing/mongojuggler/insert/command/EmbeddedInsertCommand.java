package com.futureprocessing.mongojuggler.insert.command;


import com.futureprocessing.mongojuggler.insert.InserterMapper;
import com.futureprocessing.mongojuggler.insert.InsertProxy;
import com.mongodb.BasicDBObject;

import java.util.function.Consumer;

public class EmbeddedInsertCommand extends AbstractInsertCommand {

    private final Class<?> embeddedType;
    private final InserterMapper mapper;

    public EmbeddedInsertCommand(String field, Class<?> embeddedType, InserterMapper mapper) {
        super(field);
        this.embeddedType = embeddedType;
        this.mapper = mapper;
    }

    @Override
    public void insert(BasicDBObject document, Object[] args) {
        Object embedded = InsertProxy.create(embeddedType, mapper.get());
        Consumer consumer = (Consumer) args[0];
        consumer.accept(embedded);

        document.put(field, InsertProxy.extract(embedded).getDocument());
    }

}
